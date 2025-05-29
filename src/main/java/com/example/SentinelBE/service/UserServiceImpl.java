package com.example.SentinelBE.service;

import com.example.SentinelBE.authentication.dto.ChangePasswordDto;
import com.example.SentinelBE.authentication.dto.EnableUserDto;
import com.example.SentinelBE.authentication.exception.PasswordMissmatchException;
import com.example.SentinelBE.authentication.exception.UserServiceException;
import com.example.SentinelBE.model.Role;
import com.example.SentinelBE.model.User;
import com.example.SentinelBE.repository.UserRepository;
import com.example.SentinelBE.utils.validation.GenericValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final GenericValidator<User> validator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User addUser(User user) {
        try {
            validator.validate(user);
            return this.userRepository.save(user);
        } catch (ConstraintViolationException ex){
            throw new UserServiceException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        var updatedUser = this.userRepository.findById(user.getId()).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id %d does not exist.", user.getId())
                ));
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setProfilePicture(user.getProfilePicture());

        try {
            validator.validate(user);
            return this.userRepository.save(updatedUser);
        } catch (ConstraintViolationException ex){
            throw new UserServiceException(ex.getMessage());
        }
    }

    @Override
    public void deleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return this.userRepository.findAllByRole(role.name());
    }

    @Override
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional
    public User getUserByUsername(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with username: %s, not found", username));
        }
        Hibernate.initialize(user.get().getScans());
        Hibernate.initialize(user.get().getReportedExecutables());
        return user.get();

    }

    @Override
    @Transactional
    public User getUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id: %d, not found", id));
        }
        Hibernate.initialize(user.get().getScans());
        Hibernate.initialize(user.get().getReportedExecutables());
        return user.get();
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordDto changePasswordDto) {
        var user = this.getUserByUsername(changePasswordDto.username());
        if (passwordEncoder.matches(changePasswordDto.oldPassword(), user.getHashedPassword())){
            user.setHashedPassword(passwordEncoder.encode(changePasswordDto.newPassword()));
            this.userRepository.save(user);
        } else {
            throw new PasswordMissmatchException("Current password is not correct.");
        }
    }



    @Override
    @Transactional
    public void enableUser(EnableUserDto enableUserDto) {
        var user = this.getUserByUsername(enableUserDto.username());
        user.setEnabled(enableUserDto.isEnabled());
        this.userRepository.save(user);
    }

}

package com.example.SentinelBE.service;



import com.example.SentinelBE.authentication.dto.ChangePasswordDTO;
import com.example.SentinelBE.authentication.dto.EnableUserDTO;
import com.example.SentinelBE.model.Role;
import com.example.SentinelBE.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);
    User updateUser(User user);
    void deleteUser(long id);
    List<User> getUsersByRole(Role role);
    List<User> getUsers();
    User getUserByUsername(String username);
    User getUserById(long id);
    void changePassword(ChangePasswordDTO changePasswordDto);
    void enableUser(EnableUserDTO enableUserDto);

}

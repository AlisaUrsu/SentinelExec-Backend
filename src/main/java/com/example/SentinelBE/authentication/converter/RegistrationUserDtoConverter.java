package com.example.SentinelBE.authentication.converter;



import com.example.SentinelBE.authentication.dto.RegistrationUserDTO;
import com.example.SentinelBE.model.User;
import com.example.SentinelBE.utils.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationUserDtoConverter implements Converter<User, RegistrationUserDTO> {

    @Override
    public User createFromDto(RegistrationUserDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .hashedPassword(dto.getPassword())
                .email(dto.getEmail())
                .build();

    }

    @Override
    public RegistrationUserDTO createFromEntity(User entity) {
        return null;
    }
}

package com.example.SentinelBE.authentication.converter;



import com.example.SentinelBE.authentication.dto.RegistrationUserDto;
import com.example.SentinelBE.model.User;
import com.example.SentinelBE.utils.converter.ByteStringConverter;
import com.example.SentinelBE.utils.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class RegistrationUserDtoConverter implements Converter<User, RegistrationUserDto> {

    @Override
    public User createFromDto(RegistrationUserDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .hashedPassword(dto.getPassword())
                .email(dto.getEmail())
                .build();

    }

    @Override
    public RegistrationUserDto createFromEntity(User entity) {
        return null;
    }
}

package com.example.SentinelBE.authentication.converter;


import com.example.SentinelBE.authentication.dto.UserAdminDto;
import com.example.SentinelBE.model.User;
import com.example.SentinelBE.utils.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserAdminDtoConverter implements Converter<User, UserAdminDto> {
    @Override
    public User createFromDto(UserAdminDto dto) {
        return User.builder()
                .id(dto.id())
                .username(dto.username())
                .email(dto.email())
                .isEnabled(dto.isEnabled())
                .role(dto.role())
                .profilePicture(dto.profilePicture())
                .build();
    }

    @Override
    public UserAdminDto createFromEntity(User entity) {
        return new UserAdminDto(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole(),
                entity.getProfilePicture(),
                entity.isEnabled());
    }
}

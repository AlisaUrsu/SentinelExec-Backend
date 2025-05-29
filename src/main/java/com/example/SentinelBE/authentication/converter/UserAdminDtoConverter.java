package com.example.SentinelBE.authentication.converter;


import com.example.SentinelBE.authentication.dto.UserAdminDTO;
import com.example.SentinelBE.model.User;
import com.example.SentinelBE.utils.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserAdminDtoConverter implements Converter<User, UserAdminDTO> {
    @Override
    public User createFromDto(UserAdminDTO dto) {
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
    public UserAdminDTO createFromEntity(User entity) {
        return new UserAdminDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole(),
                entity.getProfilePicture(),
                entity.isEnabled());
    }
}

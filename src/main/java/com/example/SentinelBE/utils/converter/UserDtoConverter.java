package com.example.SentinelBE.utils.converter;


import com.example.SentinelBE.authentication.dto.ModifyUserDto;
import com.example.SentinelBE.model.Scan;
import com.example.SentinelBE.model.User;
import com.example.SentinelBE.utils.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDtoConverter implements Converter<User, UserDto> {
    @Override
    public User createFromDto(UserDto dto) {

        return User.builder()
                .id(dto.id())
                .username(dto.username())
                .email(dto.email())
                .role(dto.role())
                .profilePicture(ByteStringConverter.base64ToByteArray(dto.profilePicture()))
                .build();
    }

    @Override
    public UserDto createFromEntity(User entity) {
        int uniqueExecutablesScanned = (int) entity.getScans().stream()
                .map(scan -> scan.getExecutable().getId())
                .distinct()
                .count();
        int totalReports = (int) entity.getScans().stream()
                .filter(Scan::isReported)
                .count();
        return new UserDto(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole(),
                ByteStringConverter.byteArrayToBase64(entity.getProfilePicture()),
                //null,
                entity.getScans().size(),
                uniqueExecutablesScanned,
                totalReports,

                entity.getCreatedAt()
        );
    }

    public User createFromModifyUserDto(ModifyUserDto modifyUserDto) {
        return User.builder()
                .username(modifyUserDto.username())
                .profilePicture(ByteStringConverter.base64ToByteArray(modifyUserDto.profilePicture()))
                .build();
    }
}

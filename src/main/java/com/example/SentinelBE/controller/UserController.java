package com.example.SentinelBE.controller;


import com.example.SentinelBE.authentication.dto.ChangePasswordDTO;
import com.example.SentinelBE.authentication.dto.ModifyUserDTO;
import com.example.SentinelBE.service.UserService;
import com.example.SentinelBE.utils.Result;
import com.example.SentinelBE.utils.converter.UserDtoConverter;
import com.example.SentinelBE.utils.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.base-url}/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Management")
public class UserController {
    private final UserService userService;
    private final UserDtoConverter userDtoConverter;

    @Operation(
            summary = "Get user endpoint",
            description = "Retrieves details about the current user, based on the JWT token."
    )
    @GetMapping()
    @Transactional
    public Result<UserDTO> getUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userDtoConverter.createFromEntity(this.userService.getUserByUsername(username));
        return new Result<>(true, HttpStatus.OK.value(), "Details about user served.", user);
    }

    @Operation(
            summary = "Change user password endpoint; returns message",
            description = "Updates a given user's password based on the username; requires old password for security and new password."
    )
    @PutMapping("/password")
    public Result<?> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDto) {
        this.userService.changePassword(changePasswordDto);
        return new Result<>(true, HttpStatus.OK.value(), "Password changed.", null);
    }

    @Operation(
            summary = "Modify user endpoint; returns the user",
            description = "Updates a given user's fields based on the username; returns the updated user."
    )
    @PutMapping("/{userId}/modify")
    public Result<UserDTO> modifyUser(@PathVariable Long userId, @RequestBody ModifyUserDTO modifyUserDto){
        var user = userDtoConverter.createFromModifyUserDto(modifyUserDto);
        user.setId(userId);
        UserDTO userDto = userDtoConverter.createFromEntity(userService.updateUser(user));
        return new Result<>(true, HttpStatus.OK.value(), "User updated successfully.", userDto);
    }


}

package com.example.SentinelBE.controller;


import com.example.SentinelBE.authentication.converter.UserAdminDtoConverter;
import com.example.SentinelBE.authentication.dto.EnableUserDTO;
import com.example.SentinelBE.authentication.dto.UserAdminDTO;
import com.example.SentinelBE.service.UserService;
import com.example.SentinelBE.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.base-url}/admin")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin")
public class AdminController {
    private final UserService userService;
    private final UserAdminDtoConverter userAdminDtoConverter;

    @Operation(
            summary = "Endpoint for all users",
            description = "Returns a list of all users."
    )
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result<List<UserAdminDTO>> getAllUsers() {
        var users = this.userAdminDtoConverter.createFromEntities(this.userService.getUsers());
        return new Result<>(true, HttpStatus.OK.value(), "Here is a list of all the users.", users);
    }

    @Operation(
            summary = "Get user endpoint",
            description = "Returns a user, based on the given id."
    )
    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result<UserAdminDTO> getUser(@PathVariable long id) {
        var user = this.userAdminDtoConverter.createFromEntity(this.userService.getUserById(id));
        return new Result<>(true, HttpStatus.OK.value(), String.format("Here is the user with id: %d", id), user);
    }

    @Operation(
            summary = "Enable user endpoint",
            description = "Updates a given user's account status, based on username."
    )
    @PutMapping("/users/enable")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Result<?> enableUser(@RequestBody EnableUserDTO enableUserDto) {
        this.userService.enableUser(enableUserDto);
        return new Result<>(true, HttpStatus.OK.value(), String.format("User %s enabled/disabled.", enableUserDto.username()));
    }
}

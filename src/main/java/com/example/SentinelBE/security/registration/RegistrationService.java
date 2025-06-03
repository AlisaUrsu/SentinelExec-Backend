package com.example.SentinelBE.security.registration;


import com.example.SentinelBE.model.User;

public interface RegistrationService {
    void addUser(User user);

    boolean enableUser(String token);

    void removeUser(User user);

    boolean updateUser(User user);
}

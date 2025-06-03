package com.example.SentinelBE.security.token;


import com.example.SentinelBE.model.User;

public interface TokenService {
    boolean isTokenValid(UserToken userToken);

    UserToken createToken(User user, int availability);

    UserToken getToken(String token);

    void removeToken(UserToken userToken);
}

package com.example.SentinelBE.security.user;

import com.example.SentinelBE.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JpaUserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = userRepository.findByUsername(username);

        return user.map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public UserDetails loadUserByEmail(String email){
        var user = userRepository.findByEmail(email);

        return user.map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Email is not registered;"));
    }
}

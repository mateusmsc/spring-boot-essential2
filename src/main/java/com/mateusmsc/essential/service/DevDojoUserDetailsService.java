package com.mateusmsc.essential.service;

import com.mateusmsc.essential.repository.DevDojoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevDojoUserDetailsService implements UserDetailsService {
    private final DevDojoUserRepository devDojoUserRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(devDojoUserRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

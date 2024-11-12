package com.zholtikov.cloud_dove.service.impl;


import com.zholtikov.cloud_dove.dal.UserRepository;
import com.zholtikov.cloud_dove.enums.UserStatus;
import com.zholtikov.cloud_dove.exception.UserBlockedException;
import com.zholtikov.cloud_dove.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, UserBlockedException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            log.info("Username " + username + " does not exist.");
            throw new UsernameNotFoundException("Username %s does not exist".formatted(username));
        }
        User user = userOptional.get();
        if (user.getStatus().equals(UserStatus.BLOCKED)){
            log.info("Username " + username + " is blocked.");
            throw new UserBlockedException("Username %s is blocked".formatted(username));
        }
       
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPasswordHash(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }
}

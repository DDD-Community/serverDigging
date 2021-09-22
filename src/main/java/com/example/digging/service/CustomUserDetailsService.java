package com.example.digging.service;

import com.example.digging.domain.entity.User;
import com.example.digging.domain.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String uid) {
        return userRepository.findOneWithAuthoritiesByUid(uid)
                .map(user -> createUser(user))
                .orElseThrow(() ->
                        new UsernameNotFoundException(uid + " -> 데이터베이스에서 찾을 수 없습니다.")
                );
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {
        if (!user.isActivated()) {
            String username = user.getUsername();
            throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
        }
        System.out.println(user.getUsername());
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUid(),
                user.getPassword(),
                grantedAuthorities);
    }


}
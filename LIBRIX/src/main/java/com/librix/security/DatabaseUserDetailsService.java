package com.librix.security;

import com.librix.repository.AppUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {
    private final AppUserRepository users;
    public DatabaseUserDetailsService(AppUserRepository users) { this.users = users; }

    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.findByUsernameIgnoreCase(username).map(user -> User.withUsername(user.getUsername())
                .password(user.getPasswordHash()).disabled(!user.isActive())
                .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())).build())
            .orElseThrow(() -> new UsernameNotFoundException("Account not found"));
    }
}

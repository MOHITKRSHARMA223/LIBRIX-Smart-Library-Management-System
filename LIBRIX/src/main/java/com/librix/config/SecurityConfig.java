package com.librix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    AuthenticationSuccessHandler portalSuccessHandler() {
        return (request, response, authentication) -> {
            boolean student = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"));
            response.sendRedirect(student ? "/student/dashboard" : "/admin/dashboard");
        };
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/login", "/student/login", "/admin/login", "/authenticate", "/error").permitAll()
                .requestMatchers("/student/**").hasRole("STUDENT")
                .requestMatchers("/admin/**").hasAnyRole("SUPER_ADMIN", "LIBRARIAN")
                .requestMatchers("/books/**", "/members/**", "/borrowing/**").hasAnyRole("SUPER_ADMIN", "LIBRARIAN")
                .anyRequest().authenticated())
            .formLogin(login -> login.loginPage("/student/login").loginProcessingUrl("/authenticate").successHandler(portalSuccessHandler()).failureUrl("/student/login?error").permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/student/login?logout").permitAll())
            .build();
    }
}

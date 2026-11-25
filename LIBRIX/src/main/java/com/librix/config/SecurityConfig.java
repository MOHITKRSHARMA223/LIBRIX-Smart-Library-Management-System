package com.librix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;

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
    AuthenticationFailureHandler portalFailureHandler() {
        return (request, response, exception) -> {
            String referer = request.getHeader("Referer");
            if (referer != null && referer.contains("/admin/login")) {
                response.sendRedirect("/admin/login?error");
            } else {
                response.sendRedirect("/student/login?error");
            }
        };
    }

    @Bean
    AuthenticationEntryPoint portalEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/student/login") {
            @Override
            protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
                String uri = request.getRequestURI();
                if (uri != null && (uri.startsWith("/admin") || uri.startsWith("/books") || uri.startsWith("/members") || uri.startsWith("/borrowing") || uri.startsWith("/categories") || uri.startsWith("/authors") || uri.startsWith("/publishers"))) {
                    return "/admin/login";
                }
                return "/student/login";
            }
        };
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/images/**", "/login", "/student/login", "/student/register", "/admin/login", "/authenticate", "/error").permitAll()
                .requestMatchers("/student/**").hasRole("STUDENT")
                .requestMatchers("/admin/**").hasAnyRole("SUPER_ADMIN", "LIBRARIAN")
                .requestMatchers("/books/**", "/members/**", "/borrowing/**").hasAnyRole("SUPER_ADMIN", "LIBRARIAN")
                .requestMatchers("/categories/**", "/authors/**", "/publishers/**", "/search/**", "/dashboard").hasAnyRole("SUPER_ADMIN", "LIBRARIAN")
                .anyRequest().authenticated())
            .formLogin(login -> login
                .loginPage("/student/login")
                .loginProcessingUrl("/authenticate")
                .successHandler(portalSuccessHandler())
                .failureHandler(portalFailureHandler())
                .permitAll())
            .exceptionHandling(ex -> ex.authenticationEntryPoint(portalEntryPoint()))
            .logout(logout -> logout.logoutSuccessUrl("/student/login?logout").permitAll())
            .build();
    }
}

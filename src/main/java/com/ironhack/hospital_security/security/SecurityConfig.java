package com.ironhack.hospital_security.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET,"/employees/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/employees/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,"/employees/**").hasAnyRole("ADMIN", "CONTRIBUTOR")
                .requestMatchers(HttpMethod.DELETE,"/employees/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/patients/**").permitAll()
                .requestMatchers(HttpMethod.POST,  "/patients/**").hasAnyRole("ADMIN", "CONTRIBUTOR")
                .requestMatchers(HttpMethod.PATCH, "/patients/**").hasAnyRole("ADMIN", "CONTRIBUTOR")
                .requestMatchers(HttpMethod.DELETE,"/patients/**").hasRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .userDetailsService(userDetailsService)
                .httpBasic()
                .and()
                .build();
    }
}

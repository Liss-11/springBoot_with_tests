package com.ironhack.hospital_security.security;

import com.ironhack.hospital_security.enums.Role;
import com.ironhack.hospital_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@RequiredArgsConstructor

//He probado de moquear toda la seguridad, pero no lo he conseguido, así que solo he Mokeado el role, directamente.
public class UserDetailsConfig {

    private final PasswordEncoder passwordEncoder;


    @Bean
    @Profile("demo")
    public UserDetailsService inMemoryUsers(){
        UserDetails admin = User.builder()
                .username("alissia")
                .password(passwordEncoder.encode("alissia"))
                .roles("ADMIN")
                .build();
        UserDetails contributor = User.builder()
                .username("ara")
                .password(passwordEncoder.encode("ara"))
                .roles("CONTRIBUTOR")
                .build();
        return new InMemoryUserDetailsManager(admin, contributor);
    }
}

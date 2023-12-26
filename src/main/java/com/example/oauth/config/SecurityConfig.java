package com.example.oauth.config;

import com.example.oauth.config.OAuth2.CustomSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .anyRequest()
                .authenticated();

        http
                .formLogin();

        http
                .apply(new CustomSecurityConfigurer().setFlag(false));

        return http.build();
    }
}

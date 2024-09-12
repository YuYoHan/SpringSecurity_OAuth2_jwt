package org.example.security_jwt_oauth2.config.security;

import org.example.security_jwt_oauth2.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
    public CustomUserDetails(UserEntity user) {

    }
}

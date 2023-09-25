package com.example.oauth.config.OAuth2;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Map;

public interface ProviderUser {
    String getId();
    String getUserName();
    String getPassword();
    String getEmail();
    String getProvider();
    List<SimpleGrantedAuthority> getAuthorities();
    Map<String, Object> getAttributes();
}

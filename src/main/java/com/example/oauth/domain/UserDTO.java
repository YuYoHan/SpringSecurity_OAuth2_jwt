package com.example.oauth.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String registrationId;
    private String userName;
    private String password;
    private String provider;
    private String providerId;
    private String email;
    private List<? extends GrantedAuthority> authorities;

    @Builder
    public UserDTO(Long id,
                   String registrationId,
                   String userName,
                   String password,
                   String provider,
                   String email,
                   String providerId,
                   List<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.registrationId = registrationId;
        this.userName = userName;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.authorities = authorities;
    }
}

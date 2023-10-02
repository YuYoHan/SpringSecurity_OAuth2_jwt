package com.example.oauth.entity;

import com.example.oauth.domain.Role;
import com.example.oauth.domain.UserDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@Entity
public class UserEntity {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    private String registrationId;
    private String userName;
    private String password;
    private String provider;
    private String providerId;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;


    @Builder
    public UserEntity(Long id,
                      String registrationId,
                      String userName,
                      String password,
                      String provider,
                      String providerId,
                      String email,
                      Role role) {
        this.id = id;
        this.registrationId = registrationId;
        this.userName = userName;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.role = role;
    }

    public static UserEntity toEntity(UserDTO userDTO) {
        return UserEntity.builder()
                .id(userDTO.getId())
                .registrationId(userDTO.getRegistrationId())
                .userName(userDTO.getUserName())
                .provider(userDTO.getProvider())
                .providerId(userDTO.getProviderId())
                .email(userDTO.getEmail())
                .role(userDTO.getRole())
                .build();
    }
}

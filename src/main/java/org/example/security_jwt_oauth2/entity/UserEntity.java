package org.example.security_jwt_oauth2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String userName;
    private String password;
    private String role;
}

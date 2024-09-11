package org.example.security_jwt_oauth2.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class JoinDTO {
    private String userName;
    private String password;
}

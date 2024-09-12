package org.example.security_jwt_oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.security_jwt_oauth2.dto.JoinDTO;
import org.example.security_jwt_oauth2.entity.UserEntity;
import org.example.security_jwt_oauth2.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDTO joinDTO) {

        // DB에 이미 동일한 계정이 존재하는지 체크
        boolean isUser = userRepository.existsByUserName(joinDTO.getUserName());
        if (isUser) {
            UserEntity user = UserEntity.builder()
                    .userName(joinDTO.getUserName())
                    .password(bCryptPasswordEncoder.encode(joinDTO.getPassword()))
                    .role("ROLE_USER")
                    .build();
            log.info(user);
            userRepository.save(user);
        }
    }
}

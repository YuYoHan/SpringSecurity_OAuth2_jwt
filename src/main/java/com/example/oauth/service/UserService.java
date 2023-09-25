package com.example.oauth.service;

import com.example.oauth.config.OAuth2.ProviderUser;
import com.example.oauth.domain.UserDTO;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;

    public void register(String registrationId, ProviderUser providerUser) {
        UserDTO user = UserDTO.builder().registrationId(registrationId)
                .userName(providerUser.getUserName())
                .provider(providerUser.getProvider())
                .providerId(providerUser.getId())
                .email(providerUser.getEmail())
                .authorities(providerUser.getAuthorities())
                .build();

        UserEntity userEntity = UserEntity.toEntity(user);
        UserEntity findUser = userRepository.findByUserName(userEntity.getUserName());
        log.info("user : " + user);

        if(findUser == null) {
            userRepository.save(findUser);
            log.info("가입되었습니다.");
        } else {
            log.info("이미 가입된 회원이 있습니다.");
        }

    }
}

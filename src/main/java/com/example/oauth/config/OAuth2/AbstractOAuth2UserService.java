package com.example.oauth.config.OAuth2;

import com.example.oauth.entity.UserEntity;
import com.example.oauth.repository.UserRepository;
import com.example.oauth.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Getter
@RequiredArgsConstructor
@Log4j2
// 사용자 등록과 어떤 사용자인지 알아볼 수 있는 곳
public abstract class AbstractOAuth2UserService {

    private final UserRepository userRepository;
    private final UserService userService;

    protected void register(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        UserEntity findUser = userRepository.findByUserName(providerUser.getUserName());

        if(findUser == null) {
            String registrationId = userRequest.getClientRegistration().getRegistrationId();
            userService.register(registrationId, providerUser);
        } else {
            log.info("user : " + findUser);
        }
    }

    protected ProviderUser providerUser(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        String registrationId = clientRegistration.getRegistrationId();

        if(registrationId.equals("google")) {
            return new GoogleUser(oAuth2User, clientRegistration);
        } else if(registrationId.equals("naver")) {
            return new NaverUser(oAuth2User, clientRegistration);
        } else {
            return null;
        }
    }
}

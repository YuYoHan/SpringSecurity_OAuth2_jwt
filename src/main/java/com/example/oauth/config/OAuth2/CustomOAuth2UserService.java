package com.example.oauth.config.OAuth2;

import com.example.oauth.repository.UserRepository;
import com.example.oauth.service.UserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
// OAuth2UserService : Spring Security에서 OAuth 2.0을 사용하여 인증한 사용자 정보를 가져오기 위한 인터페이스입니다.
// 이 인터페이스를 구현하여 사용자 정보를 가져오는 방법을 정의하고,
// OAuth 2.0 프로바이더(예: Google, Facebook, GitHub 등)로부터 인증된 사용자 정보를 추출할 수 있습니다.

// OAuth2UserRequest : 이 객체는 OAuth 2.0 클라이언트 정보, 권한 부여 코드, 액세스 토큰 등을 포함합니다.
// 이 정보를 사용하여 사용자 정보를 요청하고 처리할 수 있습니다.

// OAuth2User : OAuth 2.0 프로바이더(인증 제공자)로부터 가져온 인증된 사용자 정보를 나타냅니다.
// 이 정보는 사용자의 프로필 데이터, 권한(스코프), 사용자 ID 등을 포함할 수 있습니다.
public class CustomOAuth2UserService extends AbstractOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    public CustomOAuth2UserService(UserRepository userRepository, UserService userService) {
        super(userRepository, userService);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // ClientRegistration은 Spring Security에서 OAuth 2.0 또는 OpenID Connect (OIDC) 클라이언트
        // 애플리케이션의 등록 정보를 나타내는 클래스입니다. 클라이언트 애플리케이션의 설정 및 속성을 포함합니다.

        // userRequest.getClientRegistration()은 인증 및 인가된 사용자 정보를 가져오는
        // Spring Security에서 제공하는 메서드입니다.
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService =
                new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 여기에는 구글, 네이버 정보가 담겨져 있다.
        ProviderUser providerUser = super.providerUser(clientRegistration, oAuth2User);

        // 회원가입
        super.register(providerUser, userRequest);

        return oAuth2User;
    }
}

package com.example.oauth.config.OAuth2;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleUser extends OAuth2ProviderUser{

    public GoogleUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        // 사용자의 정보는 oAuth2User.getAttributes() 여기에 담겨져 있다.
        // 여기는 클레임 형식 즉, Map 형식으로 되어 있다.
        super(oAuth2User.getAttributes(), oAuth2User, clientRegistration);
    }

    @Override
    // 식별자의 역할
    public String getId() {
        return (String) getAttributes().get("sub");
    }

    @Override
    // 유저 id
    public String getUserName() {
        return (String) getAttributes().get("name");
    }
}

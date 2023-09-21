package com.example.oauth.config.OAuth2;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

// 여기는 공통적인 부분만 추상화해서 모아놓은 곳이다.
// Google, Naver에서 다른 부분들은 따로 만들어줄 것이다.
public abstract class OAuth2ProviderUser implements ProviderUser{

    private Map<String, Object> attributes;
    private OAuth2User oAuth2User;
    private ClientRegistration clientRegistration;

    public OAuth2ProviderUser(Map<String, Object> attributes,
                              OAuth2User oAuth2User,
                              ClientRegistration clientRegistration) {
        this.attributes = attributes;
        this.oAuth2User = oAuth2User;
        this.clientRegistration = clientRegistration;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public List<? extends GregorianCalendar> getAuthorities() {
        return null;
    }

    @Override
    public String getProvider() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
}

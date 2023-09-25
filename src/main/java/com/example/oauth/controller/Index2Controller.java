package com.example.oauth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class Index2Controller {

    @GetMapping("/v2")
    public String index(Model model,
                        Authentication authentication,
                        @AuthenticationPrincipal OAuth2User oAuth2User) {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        if(oAuth2AuthenticationToken != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();
            // google
            String name = (String) attributes.get("name");
            
            if(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("naver")) {
                Map<String, Object> response = (Map) attributes.get("response");
                // naver
                name = (String) response.get("name");
            }
        }
        return "";
    }
}

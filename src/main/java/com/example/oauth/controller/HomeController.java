package com.example.oauth.controller;


import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class HomeController {
    // 여기에 오려면 인증을 전부 한 상태에서 여기로 온 것이다.
    @GetMapping("/api/user")
    public String user(Authentication authentication,
                       @AuthenticationPrincipal OAuth2User oAuth2User) {
        log.info("authentication : " + authentication + ", oAuth2User : " + oAuth2User);
        return "authentication";
    }

    @GetMapping("/api/oidc")
    public String user(Authentication authentication,
                       @AuthenticationPrincipal OidcUser oidcUser) {
        log.info("authentication : " + authentication + ", oidcUser : " + oidcUser);
        return "authentication";
    }
}

package com.example.oauth.controller;

import com.example.oauth.domain.UserDTO;
import com.example.oauth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MemberController {

    private final UserService userService;


    // 회원가입
    @PostMapping("/")
    public ResponseEntity<?> signUp(@RequestBody UserDTO user) throws Exception{
        try {
            ResponseEntity<?> responseEntity = userService.signUp(user);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    // 로그인
    @PostMapping("/api/v1/users/login")
    public ResponseEntity<?> login(@RequestBody UserDTO user) throws Exception {
        try{
            String email = user.getEmail();
            String passwrod = user.getPassword();
            ResponseEntity<?> responseEntity = userService.signIn(email, passwrod);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    // 소셜 로그인
    @GetMapping("/api/v1/user/success")
    public ResponseEntity<?> socialLogin(Authentication authentication,
                                         @AuthenticationPrincipal OAuth2User oAuth2User) {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        if (oAuth2AuthenticationToken != null) {
            ResponseEntity<?> oAuth2Login =
                    userService.login(oAuth2AuthenticationToken, oAuth2User);
            return ResponseEntity.ok().body(oAuth2Login);
        }  else {
            return ResponseEntity.badRequest().build();
        }
    }
}

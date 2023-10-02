package com.example.oauth.service;

import com.example.oauth.config.OAuth2.ProviderUser;
import com.example.oauth.config.jwt.JwtAuthenticationFilter;
import com.example.oauth.config.jwt.JwtProvider;
import com.example.oauth.domain.Role;
import com.example.oauth.domain.TokenDTO;
import com.example.oauth.domain.UserDTO;
import com.example.oauth.entity.TokenEntity;
import com.example.oauth.entity.UserEntity;
import com.example.oauth.repository.TokenRepository;
import com.example.oauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

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

    // 회원가입
    public ResponseEntity<?> signUp(UserDTO user) {
        UserEntity findMember = userRepository.findByEmail(user.getEmail());

        if (findMember == null) {
            UserEntity member = UserEntity.builder()
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .userName(user.getUserName())
                    .role(user.getRole())
                    .build();

            log.info("member : " + member);
            return ResponseEntity.ok().body(member);
        } else {
            log.info("이미 가입된 회원이 있습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입된 회원이 있습니다.");
        }
    }

    // 로그인
    public ResponseEntity<?> signIn(String email, String passwrod) {
        UserEntity findUser = userRepository.findByEmail(email);

        if (findUser != null) {
            if (passwordEncoder.matches(passwrod, findUser.getPassword())) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, passwrod);
                log.info("authentication : " + authentication);

                List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(findUser);
                TokenDTO token = jwtProvider.createToken(authentication, authoritiesForUser);

                TokenEntity findToken = tokenRepository.findByMemberEmail(token.getMemberEmail());

                if (findToken == null) {
                    log.info("발급한 토큰이 없습니다.");
                    token = TokenDTO.builder()
                            .grantType(token.getGrantType())
                            .accessToken(token.getAccessToken())
                            .refreshToken(token.getRefreshToken())
                            .memberEmail(token.getMemberEmail())
                            .build();

                    TokenEntity tokenEntity = TokenEntity.toTokenEntity(token);

                    tokenRepository.save(tokenEntity);
                } else {
                    token = TokenDTO.builder()
                            .id(findToken.getId())
                            .grantType(token.getGrantType())
                            .accessToken(token.getAccessToken())
                            .refreshToken(token.getRefreshToken())
                            .memberEmail(token.getMemberEmail())
                            .build();

                    TokenEntity tokenEntity = TokenEntity.toTokenEntity(token);
                    tokenRepository.save(tokenEntity);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add(JwtAuthenticationFilter.HEADER_AUTHORIZATION, "Bearer " + token);
                return new ResponseEntity<>(token, headers, HttpStatus.OK);
            }
        }
        return ResponseEntity.notFound().build();
    }

    private List<GrantedAuthority> getAuthoritiesForUser(UserEntity findUser) {
        Role role = findUser.getRole();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        log.info("권한 : " + role.name());
        return authorities;
    }

    // 소셜 로그인
    public ResponseEntity<?> login(OAuth2AuthenticationToken oAuth2AuthenticationToken,
                                   OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("attributes : " + attributes);

        String authorizedClientRegistrationId =
                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

        String email = null;
        TokenDTO jwt = null;
        if (authorizedClientRegistrationId.equals("google")) {
            email = (String) attributes.get("email");
            log.info("email : " + email);

            jwt = createJWT(email);
            log.info("jwt : " + jwt);
        } else if (authorizedClientRegistrationId.equals("naver")) {
            Map<String, Object> response = (Map) attributes.get("response");

            email = (String) response.get("email");
            jwt = createJWT(email);
            log.info("jwt : " + jwt);
        } else {
            log.info("아무런 정보를 받지 못했습니다.");
        }
        return ResponseEntity.ok().body(jwt);
    }

    private TokenDTO createJWT(String email) {
        UserEntity findUser = userRepository.findByEmail(email);
        List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(findUser);
        TokenDTO tokenForOAuth2 = jwtProvider.createTokenForOAuth2(email, authoritiesForUser);
        TokenEntity findToken = tokenRepository.findByMemberEmail(tokenForOAuth2.getMemberEmail());

        if (findToken == null) {
            TokenEntity tokenEntity = TokenEntity.toTokenEntity(tokenForOAuth2);
            tokenRepository.save(tokenEntity);
            log.info("token : " + tokenForOAuth2);
        } else {
            tokenForOAuth2 = TokenDTO.builder()
                    .id(findToken.getId())
                    .grantType(tokenForOAuth2.getGrantType())
                    .accessToken(tokenForOAuth2.getAccessToken())
                    .refreshToken(tokenForOAuth2.getRefreshToken())
                    .memberEmail(tokenForOAuth2.getMemberEmail())
                    .build();

            TokenEntity tokenEntity = TokenEntity.toTokenEntity(tokenForOAuth2);
            tokenRepository.save(tokenEntity);
        }
        return tokenForOAuth2;
    }
}

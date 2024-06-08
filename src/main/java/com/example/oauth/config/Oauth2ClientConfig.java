package com.example.oauth.config;

import com.example.oauth.config.OAuth2.CustomBCryptPasswordEncoder;
import com.example.oauth.config.OAuth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class Oauth2ClientConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/user")
                // 둘 중 하나만 있어도 실행가능
                .access("hasAnyRole('SCOPE_profile', 'SCOPE_email')")
                .antMatchers("/api/oidc")
                // 둘 중 하나만 있어도 실행가능
                .access("hasAnyRole('SCOPE_openid')")
                .antMatchers("/v2/").permitAll()
                .anyRequest().authenticated();

        http
                .oauth2Login(Customizer.withDefaults());

        http
                .logout()
                .logoutSuccessHandler(oidcLogoutScuccessHandler())
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID");


        http
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        http
                .logout()
                .logoutSuccessUrl("/v2/");

        return http.build();
    }

    private LogoutSuccessHandler oidcLogoutScuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("http://localhost:8080/logout");

        return null;
    }

    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new CustomBCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

}

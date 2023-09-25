package com.example.oauth.config;

import com.example.oauth.config.OAuth2.CustomOAuth2UserService;
import com.example.oauth.config.OAuth2.CustomOidcUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@EnableWebSecurity
@RequiredArgsConstructor
public class Oauth2ClientConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;

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
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .oidcUserService(customOidcUserService);

        http
                .logout()
                .logoutSuccessUrl("/v2/");

        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }

}

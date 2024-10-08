package org.example.security_jwt_oauth2.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// 스프링 시큐리티라는 것을 알려줌
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // SecurityFilterChain으로 보안 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**", "/login", "/join", "/joinProc").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/my/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated()  // 나머지 모든 요청은 인증 필요
                )
                .formLogin(form -> form
                        .loginPage("/login")  // 사용자 정의 로그인 페이지
                        .loginProcessingUrl("/loginProc") // form에서 post로 이경로로 보내면 시큐리티가 처리함
                        .permitAll() // 로그인  페이지는 누구나 접근 가능
                )
                .logout(logout -> logout.permitAll()  // 로그아웃 URL 접근 허용
                )
                .csrf(auth -> auth.disable());

        http
                .sessionManagement(auth -> auth
                        // 하나의 아이디에 대한 다중 로그인 허용 개수
                        .maximumSessions(1)
                        // 다중 로그인 개수를 초과하였을 경우 처리 방법
                        // true : 초과시 새로운 로그인 차단
                        // false : 초과시 기존 세션 하나 삭제
                        .maxSessionsPreventsLogin(true));

        http
                .sessionManagement(auth -> auth
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
}

package com.example.oauth.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

import java.util.Collection;
import java.util.HashSet;

// 이거는 소셜 로그인에 권한을 주기위한 곳이다.
// 구글 같은 경우는 https://.... 이런식으로 길게 오기 때문에
// 처리하기 위한 곳이다.
public class CustomAuthorityMapper implements GrantedAuthoritiesMapper {

    private String prefix = "ROLE_";

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        HashSet<GrantedAuthority> mapped = new HashSet<>(authorities.size());
        
        for (GrantedAuthority authority : authorities) {
            mapped.add(mapAuthority(authority.getAuthority()));
        }
        return mapped;
    }

    private GrantedAuthority mapAuthority(String name) {
        if (name.lastIndexOf(".") > 0) {
            int index = name.lastIndexOf(".");
            name = "SCOPE_" + name.substring(index + 1);
        }

        if(prefix.length() > 0 && !name.startsWith(prefix)) {
            name = prefix + name;
        }
        return new SimpleGrantedAuthority(name);
    }
}

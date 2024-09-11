package org.example.security_jwt_oauth2.config.security;

import lombok.RequiredArgsConstructor;
import org.example.security_jwt_oauth2.entity.UserEntity;
import org.example.security_jwt_oauth2.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity findByUser = userRepository.findByUserName(username);

        if (findByUser != null) {
            return new CustomUserDetails(findByUser);
        }

        return null;
    }
}

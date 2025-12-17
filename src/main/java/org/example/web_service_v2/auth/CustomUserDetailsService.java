package org.example.web_service_v2.auth;

import lombok.RequiredArgsConstructor;
import org.example.web_service_v2.user.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = authRepository.findUserByEmail(email);
        return toDetails(user);
    }

    public UserDetails loadUserById(Long userId){
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return toDetails(user);
    }

    private CustomUserDetails toDetails(User user){
        List<SimpleGrantedAuthority> auth = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), auth);
    }
}

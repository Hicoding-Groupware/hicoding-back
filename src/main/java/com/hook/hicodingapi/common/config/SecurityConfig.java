package com.hook.hicodingapi.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.hook.hicodingapi.common.ApiURIConstants.BASE_PATH;
import static com.hook.hicodingapi.common.ApiURIConstants.MEMBER_PATH;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // CSRF 설정 비활성화
                .csrf()
                .disable()
                // 요청에 대한 권한 체크
//                .authorizeRequests()
//                // 클라이언트가 외부 도메인을 요청하는 경우 웹 브라우저에서 자체적으로 사전 요청(preflight)이 일어남
//                // 이 때 OPTIONS 메서드로 서버에 사전 요청을 보내 권한을 확인함
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers(HttpMethod.GET, BASE_PATH + MEMBER_PATH + "/**").permitAll()
//                .antMatchers(HttpMethod.POST, BASE_PATH + MEMBER_PATH + "/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
                .build();
    }
}

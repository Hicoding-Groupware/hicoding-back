package com.hook.hicodingapi.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hook.hicodingapi.jwt.filter.JwtAuthenticationFilter;
import com.hook.hicodingapi.jwt.handler.JwtAccessDeniedHandler;
import com.hook.hicodingapi.jwt.handler.JwtAuthenticationEntryPoint;
import com.hook.hicodingapi.jwt.service.JwtService;
import com.hook.hicodingapi.login.filter.CustomUsernamePasswordAuthenticationFilter;
import com.hook.hicodingapi.login.handler.LoginFailureHandler;
import com.hook.hicodingapi.login.handler.LoginSuccessHandler;
import com.hook.hicodingapi.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static com.hook.hicodingapi.common.ApiURIConstants.*;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final LoginService loginService;
    private final JwtService jwtService;


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                // csrf 설정 비활성화
                .csrf()
                .disable()
                // API 서버는 session을 사용하지 않으므로 STATELESS 설정
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //STATELESS 상태값 없음
                .and()
                // 요청에 대한 권한 체크
                .authorizeRequests()
                // 클라이언트가 외부 도메인을 요청하는 경우 웹 브라우저에서 자체적으로 사전 요청(preflight)이 일어남
                // 이 때 OPTIONS 메서드로 서버에 사전 요청을 보내 권한을 확인함
                // method / url / pattern
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/profileimgs/**").permitAll()  //이거 해야지 사진 나옴
                .antMatchers(HttpMethod.GET, "/msgFile/**").permitAll()
                .antMatchers(BASE_PATH + MEMBER_PATH + "/**").permitAll()
                .antMatchers(BASE_PATH + BOARD_PATH + "/**").permitAll()
                .antMatchers(BASE_PATH + COMMENT_PATH + "/**").permitAll()
                .antMatchers(BASE_PATH + "/login", BASE_PATH + "/member/pre/login", BASE_PATH +"/member/memberInfo").permitAll()
//                .antMatchers("/api/v1/products-management/**", "/api/vi/products/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
//                // 로그인 필터 설정 / 성공과 실패에 대한 핸들링
                .addFilterBefore(customUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // JWT Token 인증 필터 설정 (로그인 필터 앞에 설정)
                .addFilterBefore(jwtAuthenticationFilter(), CustomUsernamePasswordAuthenticationFilter.class)
                // exception handling 설정
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint())
                .accessDeniedHandler(jwtAccessDeniedHandler())
                .and()
                // 교차 출처 자원 공유 설정
                .cors()
                .and()
                .build();
    }

    /* CORS(Cross Origin Resource Sharing) : 교차 출처 자원 공유
     * 보안상 웹 브라우저는 다른 도메인에서 서버의 자원을 요청하는 경우 막아 놓았음.
     * 기본적으로 서버에서 클라이언트를 대상으로 리소스 허용 여부를 결정함. */


    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 로컬 React에서 오는 요청은 허용한다.
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));  // ip와 port(http 포함)(Orings)을 허용한다.
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE"));
        corsConfiguration.setAllowedHeaders(Arrays.asList(
                "Access-Control-Allow-Origin", "Access-Control-Allow-Headers",
                "Content-Type", "Authorization", "X-Requested-With", "Access-Token", "Refresh-Token"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Access-Token", "Refresh-Token", "Content-Disposition"));
        // 모든 요청 url 패턴에 대해 위의 설정을 적용한다.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    /* 비밀번호 암호화 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } // random salting

    /* 인증 매니저 빈 등록 =>
    로그인 시 사용할 password encode 설정,
    로그인 시 유저 조회하는 메소드를 가진 Service 클래스 설정*/
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder()); // 체크
        provider.setUserDetailsService(loginService);   // 조회
        return new ProviderManager(provider);
    }

    /* 로그인 실패 핸들러 빈 등록 */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(objectMapper);
    }

    /* 로그인 성공 핸들러 빈 등록 */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() { return new LoginSuccessHandler(jwtService); }

    /* 로그인 필터 빈 등록 */
    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter() {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter
                = new CustomUsernamePasswordAuthenticationFilter(objectMapper);
        /* 사용할 인증 매니저 설정 */
        customUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        /* 로그인 실패 핸들링 */
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());
        /* 로그인 성공 핸들링 */
        customUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());

        return customUsernamePasswordAuthenticationFilter;
    }

    /* JWT Token 인증 필터 빈 등록 */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService);
    }

    /* 인증 실패 핸들러 */
    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    /* 인가 실패 핸들러 */
    @Bean
    public JwtAccessDeniedHandler jwtAccessDeniedHandler() {
        return new JwtAccessDeniedHandler(objectMapper);
    }


}
package com.hook.hicodingapi.login.handler;

import com.hook.hicodingapi.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Map<String, String> memberInfo = getMemberInfo(authentication);
        log.info("로그인 성공후 인증 객체에서 꺼낸 정보 : {}", memberInfo);

        String accessToken = jwtService.createAccessToken(memberInfo);
        String refreshToken = jwtService.createRefreshToken();

        log.info("발급 된 accessToken : {}", accessToken);
        log.info("발급 된 refreshToken : {}", refreshToken);

        response.setHeader("Access-Token", accessToken);
        response.setHeader("Refresh-Token", refreshToken);
        response.setStatus(HttpServletResponse.SC_OK);  //200번 일때 나타낸다

        jwtService.updateRefreshToken(memberInfo.get("memberId"), refreshToken);

    }

    private Map<String, String> getMemberInfo(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String memberRole = userDetails.getAuthorities()
                .stream().map(auth -> auth.getAuthority().toString())
                .collect(Collectors.joining());

        return Map.of(
          "memberId", userDetails.getUsername(),
          "memberRole", memberRole
        );

    }
}

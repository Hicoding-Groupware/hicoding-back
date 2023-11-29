package com.hook.hicodingapi.jwt.filter;

import com.hook.hicodingapi.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /* 로그인 요청의 경우 다음 필터로 진행 */
        if (request.getRequestURI().equals("/login")){
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtService.getRefreshToken(request)
                .filter(jwtService::isValidToken)
                .orElse(null);
        log.info("{}", refreshToken);


         if (refreshToken != null){
             jwtService.checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
             return;
         }


         if (refreshToken == null){
             jwtService.checkAccessTokenAndAuthentication(request, response, filterChain);

         }


    }
}

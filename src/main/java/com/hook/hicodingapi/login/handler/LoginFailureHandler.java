package com.hook.hicodingapi.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hook.hicodingapi.common.exception.login.ExceptionResponse;
import com.hook.hicodingapi.common.exception.type.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.FAIL_LOGIN;

@RequiredArgsConstructor
/* 로그인 실패 */
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new ExceptionResponse(FAIL_LOGIN)));  //자바 문자를 제이슨문자열로 변환해주는 애
    }
}

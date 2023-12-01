package com.hook.hicodingapi.jwt.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hook.hicodingapi.common.exception.login.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.ACCESS_DENIED;


/* 인가 실패 처리 */
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {//인가 실패

    private final ObjectMapper objectMapper;

    /* 필요 권한이 없는데 접근하여 인가되지 않을 경우 403오류를 반환한다. */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                  response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                  response.setContentType("application/json");
                  response.setCharacterEncoding("UTF-8");
                  response.getWriter().write(objectMapper.writeValueAsString(new ExceptionResponse(ACCESS_DENIED)));
    }



}

package com.hook.hicodingapi.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
@Getter
public class CustomUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String HTTP_METHOD = "POST";

    private static final String LOGIN_REQUEST_URL = "/hc-app/v1/login";

    private static final String CONTENT_TYPE = "application/json";

    private static final String USERNAME = "memberId";
    private static final  String PASSWORD = "memberPwd";


    private final ObjectMapper objectMapper;

    //포스트 방식으로 login이라는 요청이 왔을때 아래의 필터를 호출하겠다.
    public CustomUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher( LOGIN_REQUEST_URL,HTTP_METHOD ));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)){
            throw new AuthenticationServiceException("Content-Type not supported");
        }

        String body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        Map<String, String> bodyMap = objectMapper.readValue(body, Map.class);

        String memberId = bodyMap.get(USERNAME);
        String memberPwd = bodyMap.get(PASSWORD);

        UsernamePasswordAuthenticationToken authenticationToken
                =new UsernamePasswordAuthenticationToken(memberId, memberPwd);



        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}

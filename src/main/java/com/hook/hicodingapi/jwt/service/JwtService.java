package com.hook.hicodingapi.jwt.service;

import com.hook.hicodingapi.common.exception.BadRequestException;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_MEMBER_ID;

@Service
@Slf4j
public class JwtService {
    @Value("${jwt.access.expiration}")  //만료 시간 yml 설정해둔거
    private Long accessTokenExpirationPeriod;
    @Value("${jwt.access.expiration}")  //만료 시간 yml 설정해둔거
    private Long refreshTokenExpirationPeriod;

    private final Key key;

    private final MemberRepository memberRepository;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";

    public JwtService(@Value("${jwt.secret}") String secretKey, MemberRepository memberRepository){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.memberRepository = memberRepository;
    }

    public String createAccessToken(Map<String, String> memberInfo) {
        Claims claims = Jwts.claims().setSubject(ACCESS_TOKEN_SUBJECT);
        claims.putAll(memberInfo);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken() {

        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationPeriod))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    @Transactional
    public void updateRefreshToken(String memberId, String refreshToken) {
        memberRepository.findByMemberId(memberId)
                .ifPresentOrElse(
                        member -> member.updateRefreshToken(refreshToken),
                        () -> new BadRequestException(NOT_FOUND_MEMBER_ID)
                );

    }

    public Optional<String> getRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Refresh-Token"))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> getAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Access-Token"))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public boolean isValidToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            log.error("유효하지 않은 토큰입니다 {}", e.getMessage());
            return false;
        }

    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        memberRepository.findByRefreshToken(refreshToken)
                .ifPresent(member -> {
                    String reIssuedRefreshToken = reIssuedRefreshToken(member);
                    String accessToken = createAccessToken(
                            Map.of("memberId", member.getMemberId(),"memberRole",member.getMemberRole().name())
                    );
                    response.setHeader("Access-Token", accessToken);
                    response.setHeader("Refresh-Token", reIssuedRefreshToken);
                });
    }

    private String reIssuedRefreshToken(Member member) {
        String reIssuedRefreshToken = createRefreshToken();
        member.updateRefreshToken(reIssuedRefreshToken);
        memberRepository.saveAndFlush(member);
        return reIssuedRefreshToken;

    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        getAccessToken(request)
                .filter(this::isValidToken)
                .ifPresent(accessToken -> getMemberId(accessToken)
                        .ifPresent(memberId -> memberRepository.findByMemberId(memberId)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);




    }

    private Optional<String> getMemberId(String accessToken) {
        try {
            return Optional.ofNullable(
                    Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(accessToken)
                            .getBody()
                            .get("memberId").toString()
            );
        }catch (Exception e){
            log.error("유효하지 않습니다.");
            return Optional.empty();
        }

    }

    public void saveAuthentication(Member member){

        UserDetails userDetails = User.builder()
                .username(member.getMemberId())
                .password(member.getMemberPwd())
                .roles(member.getMemberRole().name())
                .build();

        Authentication authentication
                = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }




}

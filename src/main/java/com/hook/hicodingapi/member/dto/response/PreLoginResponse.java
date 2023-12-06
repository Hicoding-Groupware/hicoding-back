package com.hook.hicodingapi.member.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;

@RequiredArgsConstructor
@Getter
public class PreLoginResponse {

    private final boolean firstLogin;
    private final String memberId;
    private final String memberName;

    public static PreLoginResponse of(boolean firstLogin, String memberId, String memberName) {
        return new PreLoginResponse(firstLogin, memberId, memberName);
    }
}
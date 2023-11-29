package com.hook.hicodingapi.member.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberGenerateResponse {
    private String memberId;
    private String memberPwd;

    public MemberGenerateResponse(String memberId, String memberPwd) {
        this.memberId = memberId;
        this.memberPwd = memberPwd;
    }
}

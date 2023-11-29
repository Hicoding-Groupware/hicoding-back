package com.hook.hicodingapi.member.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberCreationResponse {
    private String memberId;
    private String memberPwd;

    public MemberCreationResponse(String memberId, String memberPwd) {
        this.memberId = memberId;
        this.memberPwd = memberPwd;
    }
}

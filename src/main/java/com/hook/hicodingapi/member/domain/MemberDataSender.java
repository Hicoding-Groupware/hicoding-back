package com.hook.hicodingapi.member.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

@Getter
@Setter
public class MemberDataSender {
    private String memberId;
    private Integer registrationNo;

    public MemberDataSender(String memberId, Integer registrationNo) {
        this.memberId = memberId;
        this.registrationNo = registrationNo;
    }
}

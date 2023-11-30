package com.hook.hicodingapi.member.domain.type;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("01"),
    STAFF("02"),
    TEACHER("03");

    private final String roleNo;

    private MemberRole(String roleNo) {
        this.roleNo = roleNo;
    }
}

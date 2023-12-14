package com.hook.hicodingapi.member.domain.type;

import com.hook.hicodingapi.common.exception.NotFoundException;
import lombok.Getter;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_MEMBER_ROLE_CODE;

@Getter
public enum MemberRole {
    ADMIN("admin"),
    STAFF("staff"),
    TEACHER("teacher"),
    ALL("all");

    private final String roleNo;

    private MemberRole(String roleNo) {
        this.roleNo = roleNo;
    }

    public static MemberRole fromValue(String roleNo) {
        for (MemberRole type : MemberRole.values()) {
            if (type.roleNo.equalsIgnoreCase(roleNo)) {
                return type;
            }
        }

        throw new NotFoundException(NOT_FOUND_MEMBER_ROLE_CODE);
    }
}

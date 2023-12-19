package com.hook.hicodingapi.board.domain.type;

import com.hook.hicodingapi.common.exception.NotFoundException;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_BOARD_TYPE_CODE;

public enum BoardRole {
    ALL("00"),
    COMMON("01"),
    STAFF("02"),
    TEACHER("03");

    private final String roleNo;

    private BoardRole(String roleNo) {
        this.roleNo = roleNo;
    }

    public static BoardRole fromValue(String roleNo) {
        for (BoardRole type : BoardRole.values()) {
            if (type.roleNo.equalsIgnoreCase(roleNo)) {
                return type;
            }
        }

        throw new NotFoundException(NOT_FOUND_BOARD_TYPE_CODE);
    }
}

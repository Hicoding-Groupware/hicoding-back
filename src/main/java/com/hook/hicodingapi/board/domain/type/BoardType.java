package com.hook.hicodingapi.board.domain.type;

import com.hook.hicodingapi.common.exception.NotFoundException;
import lombok.Getter;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_BOARD_TYPE_CODE;
import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_STD_CODE;

@Getter
public enum BoardType {
    ALL_01("all-01"),
    ALL_02("all-02"),
    STAFF_01("staff-01"),
    STAFF_02("staff-02"),
    TEACHER_01("teacher-01"),
    TEACHER_02("teacher-02");

    private final String value;

    private BoardType(String value) { this.value = value; }

    public static BoardType fromValue(String value) {
        for (BoardType type : BoardType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new NotFoundException(NOT_FOUND_BOARD_TYPE_CODE);
    }
}

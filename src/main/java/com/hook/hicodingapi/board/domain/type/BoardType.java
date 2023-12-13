package com.hook.hicodingapi.board.domain.type;

import com.hook.hicodingapi.common.exception.NotFoundException;
import lombok.Getter;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_BOARD_TYPE_CODE;

@Getter
public enum BoardType {
    ALL_BOARD("all-board"),
    NOTICE_NOTIFICATION("notice-notification"),
    NOTICE_BOARD("notice-board"),
    STAFF_NOTIFICATION("staff-notification"),
    STAFF_BOARD("staff-board"),
    TEACHER_NOTIFICATION("teacher-notification"),
    TEACHER_BOARD("teacher-board");

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

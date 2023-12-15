package com.hook.hicodingapi.board.domain.type;

import com.hook.hicodingapi.common.exception.NotFoundException;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_BOARD_RECORD_TYPE_CODE;

public enum BoardRecordType {
    VIEWS("views"),
    LIKES("likes");

    private final String value;

    private BoardRecordType(String value) { this.value = value; }

    public static BoardRecordType fromValue(String value) {
        for (BoardRecordType type : BoardRecordType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }

        throw new NotFoundException(NOT_FOUND_BOARD_RECORD_TYPE_CODE);
    }
}

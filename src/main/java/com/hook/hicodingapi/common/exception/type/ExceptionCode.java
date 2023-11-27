package com.hook.hicodingapi.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    NOT_FOUND_STD_CODE(5000, "학생코드에 해당하는 해당 학생이 존재하지 않습니다.");

    private final int code;
    private final String message;


}

package com.hook.hicodingapi.common.exception;

import com.hook.hicodingapi.common.exception.type.ExceptionCode;

public class ConflictException extends CustomException {

    public ConflictException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}

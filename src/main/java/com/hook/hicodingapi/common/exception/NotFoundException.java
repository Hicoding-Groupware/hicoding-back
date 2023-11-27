package com.hook.hicodingapi.common.exception;

import com.hook.hicodingapi.common.exception.type.ExceptionCode;

public class NotFoundException extends CustomException {
    public NotFoundException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}

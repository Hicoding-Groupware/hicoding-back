package com.hook.hicodingapi.common.exception;

import com.hook.hicodingapi.common.exception.type.ExceptionCode;

public class InvalidDateException extends CustomException {
    public InvalidDateException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

}

package com.hook.hicodingapi.common.exception.login;

import com.hook.hicodingapi.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ExceptionResponse {

    private int code;
    private String message;

    public ExceptionResponse(final ExceptionCode exceptionCode){
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}

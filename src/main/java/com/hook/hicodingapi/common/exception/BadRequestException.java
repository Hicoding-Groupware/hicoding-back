package com.hook.hicodingapi.common.exception;

import com.hook.hicodingapi.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class BadRequestException extends CustomException{

    public BadRequestException(final ExceptionCode exceptionCode){
        super(exceptionCode);

    }
}

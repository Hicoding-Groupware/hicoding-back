package com.hook.hicodingapi.common.exception;


import com.hook.hicodingapi.common.exception.type.ExceptionCode;
import lombok.Getter;

@Getter
public class ServerInternalException extends CustomException{

    public ServerInternalException(final ExceptionCode exceptionCode) {
        super(exceptionCode);
    }


}

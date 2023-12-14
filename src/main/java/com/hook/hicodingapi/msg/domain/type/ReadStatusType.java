package com.hook.hicodingapi.msg.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReadStatusType {

    READED("readed"),
    NOTREAD("notRead");

    private final String value;


    ReadStatusType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ReadStatusType from(String value) {
        for(ReadStatusType status : ReadStatusType.values()) {
            if(status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {return value;}
}

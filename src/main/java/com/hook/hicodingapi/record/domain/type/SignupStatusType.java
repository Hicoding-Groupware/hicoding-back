package com.hook.hicodingapi.record.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SignupStatusType {

    NORMAL("normal"),
    WITHDRAW("withdraw");

    private final String value;

    SignupStatusType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static SignupStatusType from(String value) {
        for (SignupStatusType status : SignupStatusType.values()) {
            if(status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {
        return value;
    }
}

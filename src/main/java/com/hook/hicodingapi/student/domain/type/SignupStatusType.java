package com.hook.hicodingapi.student.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SignupStatusType {

    수강중("normal"),
    수강철회("withdraw");

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

package com.hook.hicodingapi.file.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AccessStatus {

    MESSAGE("message");

    private final String value;

    AccessStatus(String value) { this.value = value; }

    // Json convert하는 작업
    @JsonCreator
    public static AccessStatus from(String value) {
        for(AccessStatus status : AccessStatus.values()) {
            if(status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() { return value; }
}

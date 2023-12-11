package com.hook.hicodingapi.msg.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SenderStatusType {

    SENDER_USABLE("senderUsable"),
    SENDER_DELETED("senderDeleted");

    private final String value;


    SenderStatusType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static SenderStatusType from(String value) {
        for(SenderStatusType status : SenderStatusType.values()) {
            if(status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
   private String getValue() {return value;}

}

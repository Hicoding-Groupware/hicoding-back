package com.hook.hicodingapi.msg.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReceiverStatusType {

    RECEIVER_USABLE("receiverUsable"),
    RECEIVER_DELETED("receiverDeleted");

    private final String value;


    ReceiverStatusType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ReceiverStatusType from(String value) {
        for(ReceiverStatusType status : ReceiverStatusType.values()) {
            if(status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {return value;}
}

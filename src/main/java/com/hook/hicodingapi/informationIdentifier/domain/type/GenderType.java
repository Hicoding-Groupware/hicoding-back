package com.hook.hicodingapi.informationIdentifier.domain.type;

import lombok.Getter;

@Getter
public enum GenderType {
    MALE("남자"),
    FEMALE("여자");

    private final String gender;

    private GenderType(String gender) {
        this.gender = gender;
    }
}

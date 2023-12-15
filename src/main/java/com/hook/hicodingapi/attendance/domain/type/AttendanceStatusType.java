package com.hook.hicodingapi.attendance.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AttendanceStatusType {

    ATTENDANCE("attendance"), // 출석
    ABSENCE("absence"), // 결석
    TARDINESS("tardiness"), // 조퇴
    LEAVE_EARLY("leave_early"), // 지각
    SICK_LEAVE("sick_leave"); // 병결

    private final String value;

    AttendanceStatusType(String value) { this.value = value; }

    @JsonCreator
    public static AttendanceStatusType from(String value) {
        for(AttendanceStatusType status : AttendanceStatusType.values()) {
            if(status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() { return value; }
}

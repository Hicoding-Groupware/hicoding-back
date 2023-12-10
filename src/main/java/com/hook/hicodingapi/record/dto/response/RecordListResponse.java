package com.hook.hicodingapi.record.dto.response;

import com.hook.hicodingapi.record.domain.Record;
import com.hook.hicodingapi.record.domain.type.SignupStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class RecordListResponse {

    private final Long recCode;
    private final SignupStatusType status;
    private final Long stdCode;
    private final Long cosCode;

    public static RecordListResponse from(Record record) {
        return new RecordListResponse(
                record.getRecCode(),
                record.getSignupStatus(),
                record.getStdCode(),
                record.getCourse().getCosCode()
        );
    }
}

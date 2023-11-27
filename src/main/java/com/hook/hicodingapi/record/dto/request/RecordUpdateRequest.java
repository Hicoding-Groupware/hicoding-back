package com.hook.hicodingapi.record.dto.request;

import com.hook.hicodingapi.record.domain.type.SignupStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
public class RecordUpdateRequest {

    @Min(value = 1)
    private final Long stdCode;

    @Min(value = 1)
    private final Long cosCode;

    @NotNull
    private final SignupStatusType signupStatus;

}

package com.hook.hicodingapi.record.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class StudentCosRegistRequest {

    @Min(value = 1)
    private final Long stdCode;

    @Min(value = 1)
    private final Long cosCode;


}

package com.hook.hicodingapi.msg.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class MessageCreateRequest {

    @Min(value = 1)
    private final Long receiver;

    @NotBlank
    private final String msgContent;
}

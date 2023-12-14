package com.hook.hicodingapi.msg.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class MessageCreateRequest {

    @NotEmpty
    private final List<Long> receivers;

    @NotBlank
    private final String msgContent;


}

package com.hook.hicodingapi.msg.dto.request;

import com.hook.hicodingapi.msg.domain.type.ReceiverStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;




@RequiredArgsConstructor
@Getter
public class MessageReceiverDeleteRequest {

    @NotNull
    private final ReceiverStatusType receiverStatus;
}

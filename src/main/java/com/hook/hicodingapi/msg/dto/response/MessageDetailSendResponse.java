package com.hook.hicodingapi.msg.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MessageDetailSendResponse {

    private final Long msgNo;
    private final String receiver;
    private final LocalDateTime sendedAt;
    private final String msgContent;
    private final String fileName;
    private final String fileUrl;
}

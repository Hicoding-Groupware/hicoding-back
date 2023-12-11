package com.hook.hicodingapi.msg.dto.response;

import com.hook.hicodingapi.msg.domain.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MessageResponse {

    private final Long msgNo;
    private final String memberName;
    private final String msgContent;
    private final LocalDateTime sendedAt;


    public static MessageResponse from(Message message) {

        return new MessageResponse(
                message.getMsgNo(),
                message.getSender().getMemberName(),
                message.getMsgContent(),
                message.getSendedAt()

        );
    }
}

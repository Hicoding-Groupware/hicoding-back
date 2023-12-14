package com.hook.hicodingapi.msg.dto.response;

import com.hook.hicodingapi.msg.domain.Message;
import com.hook.hicodingapi.msg.domain.type.ReadStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MessageSendResponse {

    private final Long msgNo;
    private final String receiver;
    private final String msgContent;
    private final LocalDateTime readAt;
    private final String fileName;
    private final String fileUrl;
    private final Long fileNo;
    private final ReadStatusType readStatus;

    public static MessageSendResponse from(Message message) {
        // file이 null일때 null로 가져옴
        String fileName = "";
        String fileUrl = "";

        if(message.getFile() != null) {
            fileName = message.getFile().getFileName();
            fileUrl = message.getFile().getFileUrl();
        }


        return new MessageSendResponse(
                message.getMsgNo(),
                message.getReceiver().getMemberName(),
                message.getMsgContent(),
                message.getReadAt(),
                fileName,
                fileUrl,
                message.getFile().getFileNo(),
                message.getReadStatus()
        );
    }
}

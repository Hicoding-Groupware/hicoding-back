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
    private final String memberId;
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
        Long fileNo = null;

        if(message.getFile() != null) {
            fileName = message.getFile().getFileName();
            fileUrl = message.getFile().getFileUrl();
            fileNo = message.getFile().getFileNo();
        }


        return new MessageSendResponse(
                message.getMsgNo(),
                message.getReceiver().getMemberName(),
                message.getReceiver().getMemberId(),
                message.getMsgContent(),
                message.getReadAt(),
                fileName,
                fileUrl,
                fileNo,
                message.getReadStatus()
        );
    }
}

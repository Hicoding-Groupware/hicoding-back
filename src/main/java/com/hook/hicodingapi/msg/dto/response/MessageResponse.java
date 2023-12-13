package com.hook.hicodingapi.msg.dto.response;

import com.hook.hicodingapi.file.domain.File;
import com.hook.hicodingapi.msg.domain.Message;
import com.hook.hicodingapi.msg.domain.type.ReadStatusType;
import com.hook.hicodingapi.msg.domain.type.ReceiverStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MessageResponse {

    private final Long msgNo;
    private final String sender;
    private final String msgContent;
    private final LocalDateTime sendedAt;
    private final String fileName;
    private final String fileUrl;
    private final ReadStatusType readStatus;



    public static MessageResponse from(Message message) {

        // file이 null일때 null로 가져옴
        String fileName = "";
        String fileUrl = "";

        if(message.getFile() != null) {
            fileName = message.getFile().getFileName();
            fileUrl = message.getFile().getFileUrl();
        }



        return new MessageResponse(
                message.getMsgNo(),
                message.getSender().getMemberName(),
                message.getMsgContent(),
                message.getSendedAt(),
                fileName,
                fileUrl,
                message.getReadStatus()
        );
    }
}

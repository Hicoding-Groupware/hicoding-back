package com.hook.hicodingapi.msg.dto.response;

import com.hook.hicodingapi.msg.domain.Message;
import com.hook.hicodingapi.msg.domain.type.ReadStatusType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MessageReceiveResponse {

    private final Long msgNo;
    private final String sender;
    private final String memberId;
    private final String msgContent;
    private final LocalDateTime sendedAt;
    private final String fileName;
    private final String fileUrl;
    private final Long fileNo;



    public static MessageReceiveResponse from(Message message) {

        // file이 null일때 null로 가져옴
        String fileName = "";
        String fileUrl = "";
        Long fileNo = null;

        if(message.getFile() != null) {
            fileName = message.getFile().getFileName();
            fileUrl = message.getFile().getFileUrl();
            fileNo = message.getFile().getFileNo();
        }



        return new MessageReceiveResponse(
                message.getMsgNo(),
                message.getSender().getMemberName(),
                message.getSender().getMemberId(),
                message.getMsgContent(),
                message.getSendedAt(),
                fileName,
                fileUrl,
                fileNo
        );
    }
}

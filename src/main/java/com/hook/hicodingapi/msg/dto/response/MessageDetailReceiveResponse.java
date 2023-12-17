package com.hook.hicodingapi.msg.dto.response;

import com.hook.hicodingapi.msg.domain.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MessageDetailReceiveResponse {

    private final Long msgNo;
    private final String sender;
    private final Long memberNo;
    private final String memberId;
    private final LocalDateTime sendedAt;
    private final String msgContent;
    private final String fileName;
    private final String fileUrl;
    private final Long fileNo;

    public static MessageDetailReceiveResponse from(Message message) {

        // file이 null일때 null로 가져옴
        String fileName = "";
        String fileUrl = "";
        Long fileNo = null;

        if(message.getFile() != null) {
            fileName = message.getFile().getFileName();
            fileUrl = message.getFile().getFileUrl();
            fileNo = message.getFile().getFileNo();
        }

        return new MessageDetailReceiveResponse(
                message.getMsgNo(),
                message.getSender().getMemberName(),
                message.getSender().getMemberNo(),
                message.getSender().getMemberId(),
                message.getSendedAt(),
                message.getMsgContent(),
                fileName,
                fileUrl,
                fileNo
        );
    }
}

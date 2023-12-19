package com.hook.hicodingapi.main.message.response;

import com.hook.hicodingapi.msg.domain.Message;
import com.hook.hicodingapi.msg.domain.type.ReadStatusType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class MainMessageResponse {

    private final Long msgNo;
    private final String sender;
    private final String memberProfile;
    private final String msgContent;
    private final LocalDateTime sendedAt;
    private final String fileName;
    private final String fileUrl;
    private final ReadStatusType readStatus;




    public static MainMessageResponse from(Message message) {

        // file이 null일때 null로 가져옴
        String fileName = "";
        String fileUrl = "";

        if(message.getFile() != null) {
            fileName = message.getFile().getFileName();
            fileUrl = message.getFile().getFileUrl();
        }



        return new MainMessageResponse(
                message.getMsgNo(),
                message.getSender().getMemberName(),
                message.getSender().getMemberProfile(),
                message.getMsgContent(),
                message.getSendedAt(),
                fileName,
                fileUrl,
                message.getReadStatus()
        );
    }
}

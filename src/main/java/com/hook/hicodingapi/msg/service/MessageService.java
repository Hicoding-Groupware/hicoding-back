package com.hook.hicodingapi.msg.service;

import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.common.exception.type.ExceptionCode;
import com.hook.hicodingapi.file.domain.repository.FileRepository;
import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import com.hook.hicodingapi.msg.domain.Message;
import com.hook.hicodingapi.msg.dto.request.MessageCreateRequest;
import com.hook.hicodingapi.msg.domain.repository.MessageRepository;
import com.hook.hicodingapi.msg.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class MessageService {

    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final FileRepository fileRepository;

    @Value("${file.msgFile-dir}")
    private String FILE_DIR;


    public Long send(MessageCreateRequest messageRequest, MultipartFile msgFile, CustomUser customUser) {

        // 로그인한 memberNo
        Member sender = memberRepository.getReferenceById(customUser.getMemberNo());
        // 보낼사람 memberNo 조회
        Member receiver = memberRepository.getReferenceById(messageRequest.getReceiver());

        final Message newMessage = Message.of(
                messageRequest.getMsgContent(),
                sender,
                receiver
        );

        final Message message = messageRepository.save(newMessage);

        if(msgFile != null) {
            String fileUrl = FILE_DIR; // fileUrl
            File dir = new File(fileUrl);

            if (!dir.exists()) dir.mkdirs(); // 디렉토리 생성

            String originFileName = msgFile.getOriginalFilename();  // fileName 파일 이름
            String extension = originFileName.substring(originFileName.lastIndexOf("."));   // extension 확장자
            Long fileSize = msgFile.getSize();  // fileSize 파일 사이즈
            String savedName = UUID.randomUUID().toString().replace("-", "") + extension;

            try {
                msgFile.transferTo(new File(fileUrl + "/" + savedName));

                //tbl_file에 저장
                com.hook.hicodingapi.file.domain.File newFile = com.hook.hicodingapi.file.domain.File.of(
                        originFileName,
                        extension,
                        fileSize.toString(),
                        message,
                        fileUrl + "/" + savedName
                );

                fileRepository.save(newFile);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return message.getMsgNo();
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page -1, 15, Sort.by("memberNo").descending());
    }
    public Page<MessageResponse> getMsgs(Integer page, CustomUser customUser) {

        Page<Message> msgs = messageRepository.findByMemberNo(getPageable(page), customUser.getMemberNo());

        return msgs.map(message -> MessageResponse.from(message));
    }
}

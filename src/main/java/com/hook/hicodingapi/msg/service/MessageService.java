package com.hook.hicodingapi.msg.service;


import com.hook.hicodingapi.common.exception.ConflictException;
import com.hook.hicodingapi.common.exception.FileStorageException;
import com.hook.hicodingapi.common.exception.MyFileNotFoundException;
import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.file.domain.repository.FileRepository;
import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import com.hook.hicodingapi.msg.domain.Message;
import com.hook.hicodingapi.msg.dto.request.MessageCreateRequest;
import com.hook.hicodingapi.msg.domain.repository.MessageRepository;
import com.hook.hicodingapi.msg.dto.request.MessageReceiveDelete;
import com.hook.hicodingapi.msg.dto.request.MessageSendDelete;
import com.hook.hicodingapi.msg.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.ACCESS_DENIED;
import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_FILE_NO;
import static com.hook.hicodingapi.msg.domain.type.ReadStatusType.NOTREAD;
import static com.hook.hicodingapi.msg.domain.type.ReceiverStatusType.RECEIVER_USABLE;
import static com.hook.hicodingapi.msg.domain.type.SenderStatusType.SENDER_USABLE;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MessageService {

   
    private final   MemberRepository memberRepository;
    private final   MessageRepository messageRepository;
    private final   FileRepository fileRepository;


    @Value("${file.msgFile-dir}")
    private String FILE_DIR;
    @Value("${file.msgFile-url}")
    private String FILE_URL;


    public void send(MessageCreateRequest messageRequest, MultipartFile msgFile, CustomUser customUser) {

        // 로그인한 memberNo
        Member sender = memberRepository.getReferenceById(customUser.getMemberNo());
        // 보낼사람들 각각 memberNo 조회

        List<Member> receivers = Optional.ofNullable(messageRequest.getReceivers())
                .orElse(Collections.emptyList())
                .stream()
                .map(memberRepository::getReferenceById)
                .collect(Collectors.toList());
        log.info("receivers : {}", receivers);

        List<Message> messages = new ArrayList<>();

        for (Member receiver : receivers) {
            Message newMessage = Message.of(messageRequest.getMsgContent(), sender, receiver);
            log.info("newMessage : {}", newMessage);
            messages.add(messageRepository.save(newMessage));
        }


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

                for (Message message : messages) {
                    //tbl_file에 저장
                    com.hook.hicodingapi.file.domain.File newFile = com.hook.hicodingapi.file.domain.File.of(
                            originFileName,
                            extension,
                            fileSize,
                            message,
                            FILE_URL + savedName
                    );

                    fileRepository.save(newFile);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public Resource loadFileResource(Long fileNo) {
        com.hook.hicodingapi.file.domain.File file = fileRepository.findById(fileNo)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_FILE_NO));

        String changeFileName = file.getFileUrl().replace("http://localhost:8282/msgFile/", "");

        Path filePath = Paths.get(FILE_DIR).resolve(changeFileName);

        try {
            // 파일 리소스 생성
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found or cannot be read: " + changeFileName);
            }
        } catch (MalformedURLException e) {
            throw new FileStorageException("Could not read file: " + changeFileName, e);
        }
    }

    public String getFileName(Long fileNo) {

        com.hook.hicodingapi.file.domain.File file = fileRepository.findById(fileNo)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_FILE_NO));

        String fileName = file.getFileName();

        return fileName;
    }


    public void getEquals(Long fileNo, CustomUser customUser) {
        // 파일No를 사용하여 파일 엔티티 가져오기
        com.hook.hicodingapi.file.domain.File file = fileRepository.findById(fileNo).orElseThrow(() -> new NotFoundException(NOT_FOUND_FILE_NO));

        // 해당 파일에 대한 message 엔티티 가져오기
        Message message = file.getMessage();

        // 현재 사용자와 sender, receiver 비교
        if (!compare(customUser, message)) {
            // 현재 사용자가 sender, receiver 아니면 다운로드 권한이 없음
            throw new ConflictException(ACCESS_DENIED);
        }

    }

    private boolean compare(CustomUser customUser, Message message) {
        // 메시지의 sender, receiver 현재 사용자의 MemberNo를 비교하여 권한 확인
        return customUser.getMemberNo().equals(message.getSender().getMemberNo()) ||
                customUser.getMemberNo().equals(message.getReceiver().getMemberNo());
    }

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page -1, 15, Sort.by("msgNo").descending());
    }

    @Transactional(readOnly = true)
    public Page<MessageReceiveResponse> getReceiveMsgs(Integer page, String memberName, String content, LocalDate startDate, LocalDate endDate, CustomUser customUser) {

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (startDate != null) {
            startDateTime = startDate.atStartOfDay();
        }
        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX);
        }

        if (startDate == null && endDate == null && memberName == null && content == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatus(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE);
            return msgs.map(message -> MessageReceiveResponse.from(message));
        }
        // 최근 메세지, 보낸 시작날짜 조회
        else if (endDate == null && memberName == null && content == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatusAndSendedAtGreaterThanEqual(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE, startDateTime);
            return msgs.map(message -> MessageReceiveResponse.from(message));
            // 최근 메세지, 보낸 이전날짜 조회
        } else if (startDate == null && memberName == null && content == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatusAndSendedAtLessThanEqual(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE, endDateTime);
            return msgs.map(message -> MessageReceiveResponse.from(message));
            // 최근 메세지, 보낸 기간조회
        } else if (memberName == null && content == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatusAndSendedAtBetween(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE, startDateTime, endDateTime);
            return msgs.map(message -> MessageReceiveResponse.from(message));
            // 최근 메세지, 이름조회
        } else if (content == null && startDate == null && endDate == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatusAndSenderMemberNameContaining(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE, memberName);
            return msgs.map(message -> MessageReceiveResponse.from(message));
        }
        // 최근 메세지, 이름조회, 보낸 시작날짜
        else if (content == null && endDate == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatusAndSenderMemberNameContainingAndSendedAtGreaterThanEqual(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE, memberName, startDateTime);
            return msgs.map(message -> MessageReceiveResponse.from(message));
            // 최근 메세지, 이름조회, 보낸 이전날짜
        } else if (content == null && startDate == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatusAndSenderMemberNameContainingAndSendedAtLessThanEqual(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE, memberName, endDateTime);
            return msgs.map(message -> MessageReceiveResponse.from(message));
            // 최근 메세지, 이름조회, 보낸 기간조회
        } else if (content == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatusAndSenderMemberNameContainingAndSendedAtBetween(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE, memberName, startDateTime, endDateTime);
            return msgs.map(message -> MessageReceiveResponse.from(message));
            // 최근 메세지, 내용조회
        } else if (memberName == null && startDate == null && endDate == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatusAndMsgContentContaining(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE, content);
            return msgs.map(message -> MessageReceiveResponse.from(message));
            // 최근 메세지, 내용조회, 보낸 시작날짜
        } else if (memberName == null && endDate == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatusAndMsgContentContainingAndSendedAtGreaterThanEqual(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE, content, startDateTime);
            return msgs.map(message -> MessageReceiveResponse.from(message));
            // 최근 메세지, 내용조회, 보낸 이전날짜
        } else if (memberName == null && startDate == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatusAndMsgContentContainingAndSendedAtLessThanEqual(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE, content, endDateTime);
            return msgs.map(message -> MessageReceiveResponse.from(message));
            // 최근 메세지, 내용조회, 보낸 기간조회
        } else if (memberName == null) {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatusAndMsgContentContainingAndSendedAtBetween(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE, content, startDateTime, endDateTime);
            return msgs.map(message -> MessageReceiveResponse.from(message));
        } else {
            Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatus(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE);
            return msgs.map(message -> MessageReceiveResponse.from(message));
        }

    }



    public MessageDetailReceiveResponse getReceiveDetailMsgs(Long msgNo, CustomUser customUser) {

        // 먼저 쪽지 조회(로그인한 사람 받은쪽지 전체 조회)
        Message messages = messageRepository.findByReceiverMemberNoAndMsgNo(customUser.getMemberNo(), msgNo);

        // 메세지 업데이트(ReadAt = 현재시간, ReadStatus = READED)
        // 읽음 상태가 NOTREAD일 때만 업데이트 수행
        if (messages.getReadStatus() == NOTREAD) {
            // 메세지 업데이트(ReadAt = 현재시간, ReadStatus = READED)
            messages.update();
            log.info("readAt : {}", messages.getReadAt());
            log.info("readStatus : {}", messages.getReadStatus());
            //messageRepository.save(messages);
        }

        final Message message = messageRepository.findByReceiverMemberNoAndMsgNo(customUser.getMemberNo(), msgNo);

        return MessageDetailReceiveResponse.from(message);
    }

    public Page<MessageSendResponse> getSendMsgs(Integer page, String memberName, String content, LocalDate startDate, LocalDate endDate, CustomUser customUser) {
        LocalDateTime startedDateTime = null;
        LocalDateTime endedDateTime = null;

        if (startDate != null) {
            startedDateTime = startDate.atStartOfDay();
        }
        if (endDate != null) {
            endedDateTime = endDate.atTime(LocalTime.MAX);
        }

        if (startDate == null && endDate == null && memberName == null && content == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatus(getPageable(page), customUser.getMemberNo(), SENDER_USABLE);
            return msgs.map(message -> MessageSendResponse.from(message));
        }
        // 최근 메세지, 보낸 시작날짜 조회
        else if (endDate == null && memberName == null && content == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatusAndReadAtGreaterThanEqual(getPageable(page), customUser.getMemberNo(), SENDER_USABLE, startedDateTime);
            return msgs.map(message -> MessageSendResponse.from(message));
            // 최근 메세지, 보낸 이전날짜 조회
        } else if (startDate == null && memberName == null && content == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatusAndReadAtLessThanEqual(getPageable(page), customUser.getMemberNo(), SENDER_USABLE, endedDateTime);
            return msgs.map(message -> MessageSendResponse.from(message));
            // 최근 메세지, 보낸 기간조회
        } else if (memberName == null && content == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatusAndReadAtBetween(getPageable(page), customUser.getMemberNo(), SENDER_USABLE, startedDateTime, endedDateTime);
            return msgs.map(message -> MessageSendResponse.from(message));
            // 최근 메세지, 이름조회
        } else if (content == null && startDate == null && endDate == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatusAndReceiverMemberNameContaining(getPageable(page), customUser.getMemberNo(), SENDER_USABLE, memberName);
            return msgs.map(message -> MessageSendResponse.from(message));
        }
        // 최근 메세지, 이름조회, 보낸 시작날짜
        else if (content == null && endDate == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatusAndReceiverMemberNameContainingAndReadAtGreaterThanEqual(getPageable(page), customUser.getMemberNo(), SENDER_USABLE, memberName, startedDateTime);
            return msgs.map(message -> MessageSendResponse.from(message));
            // 최근 메세지, 이름조회, 보낸 이전날짜
        } else if (content == null && startDate == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatusAndReceiverMemberNameContainingAndReadAtLessThanEqual(getPageable(page), customUser.getMemberNo(), SENDER_USABLE, memberName, endedDateTime);
            return msgs.map(message -> MessageSendResponse.from(message));
            // 최근 메세지, 이름조회, 보낸 기간조회
        } else if (content == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatusAndReceiverMemberNameContainingAndReadAtBetween(getPageable(page), customUser.getMemberNo(), SENDER_USABLE, memberName, startedDateTime, endedDateTime);
            return msgs.map(message -> MessageSendResponse.from(message));
            // 최근 메세지, 내용조회
        } else if (memberName == null && startDate == null && endDate == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatusAndMsgContentContaining(getPageable(page), customUser.getMemberNo(), SENDER_USABLE, content);
            return msgs.map(message -> MessageSendResponse.from(message));
            // 최근 메세지, 내용조회, 보낸 시작날짜
        } else if (memberName == null && endDate == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatusAndMsgContentContainingAndReadAtGreaterThanEqual(getPageable(page), customUser.getMemberNo(), SENDER_USABLE, content, startedDateTime);
            return msgs.map(message -> MessageSendResponse.from(message));
            // 최근 메세지, 내용조회, 보낸 이전날짜
        } else if (memberName == null && startDate == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatusAndMsgContentContainingAndReadAtLessThanEqual(getPageable(page), customUser.getMemberNo(), SENDER_USABLE, content, endedDateTime);
            return msgs.map(message -> MessageSendResponse.from(message));
            // 최근 메세지, 내용조회, 보낸 기간조회
        } else if (memberName == null) {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatusAndMsgContentContainingAndReadAtBetween(getPageable(page), customUser.getMemberNo(), SENDER_USABLE, content, startedDateTime, endedDateTime);
            return msgs.map(message -> MessageSendResponse.from(message));
        } else {
            Page<Message> msgs = messageRepository.findBySenderMemberNoAndSenderStatus(getPageable(page), customUser.getMemberNo(), SENDER_USABLE);
            return msgs.map(message -> MessageSendResponse.from(message));
        }
    }

    public MessageDetailSendResponse getSendDetailMsgs(Long msgNo, CustomUser customUser) {

        final Message message = messageRepository.findBySenderMemberNoAndMsgNo(customUser.getMemberNo(), msgNo);
        return MessageDetailSendResponse.from(message);
    }

    public void receiverDelete(MessageReceiveDelete messageReceiveDelete, CustomUser customUser) {
        List<Long> msgNos = messageReceiveDelete.getMsgNos();

        for (Long msgNo : msgNos) {
            Message message = messageRepository.findByReceiverMemberNoAndMsgNoAndReceiverStatus(customUser.getMemberNo(), msgNo, RECEIVER_USABLE);
                message.receiverDelete();
        }
    }

    public void senderDelete(MessageSendDelete messageSendDelete, CustomUser customUser) {
        List<Long> msgNos = messageSendDelete.getMsgNos();

        for (Long msgNo : msgNos) {
            Message message = messageRepository.findBySenderMemberNoAndMsgNoAndSenderStatus(customUser.getMemberNo(), msgNo, SENDER_USABLE);

            message.senderDelete();
        }
    }

    @Transactional(readOnly = true)
    public List<MemberListResponse> getMemberList(String memberName) {

        List<Member> members = memberRepository.findByMemberNameContaining(memberName);
        return members.stream()
                .map(member -> MemberListResponse.from(member))
                .collect(Collectors.toList());
    }
}

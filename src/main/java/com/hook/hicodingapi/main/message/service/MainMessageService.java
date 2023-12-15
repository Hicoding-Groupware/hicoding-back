package com.hook.hicodingapi.main.message.service;

import com.hook.hicodingapi.file.domain.repository.FileRepository;
import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.main.message.response.MainMessageResponse;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import com.hook.hicodingapi.msg.domain.Message;
import com.hook.hicodingapi.msg.domain.repository.MessageRepository;
import com.hook.hicodingapi.msg.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hook.hicodingapi.msg.domain.type.ReceiverStatusType.RECEIVER_USABLE;

@RequiredArgsConstructor
@Service
@Transactional
public class MainMessageService {

    private final MessageRepository messageRepository;



    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page -1, 3, Sort.by("msgNo").descending());
    }

    @Transactional(readOnly = true)
    public Page<MainMessageResponse> getMsgs(Integer page, CustomUser customUser) {

        Page<Message> msgs = messageRepository.findByReceiverMemberNoAndReceiverStatus(getPageable(page), customUser.getMemberNo(), RECEIVER_USABLE);

        return msgs.map(message -> MainMessageResponse.from(message));
    }
}

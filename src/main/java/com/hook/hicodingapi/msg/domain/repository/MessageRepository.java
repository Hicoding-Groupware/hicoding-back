package com.hook.hicodingapi.msg.domain.repository;

import com.hook.hicodingapi.jwt.CustomUser;
import com.hook.hicodingapi.msg.domain.Message;
import com.hook.hicodingapi.msg.domain.type.ReadStatusType;
import com.hook.hicodingapi.msg.domain.type.ReceiverStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByReceiverMemberNoAndReceiverStatus(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType);

    Message findByReceiverMemberNoAndMsgNo(Long memberNo, Long msgNo);



}

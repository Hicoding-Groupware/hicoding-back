package com.hook.hicodingapi.msg.domain.repository;

import com.hook.hicodingapi.msg.domain.Message;
import com.hook.hicodingapi.msg.domain.type.ReceiverStatusType;
import com.hook.hicodingapi.msg.domain.type.SenderStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Page<Message> findByReceiverMemberNoAndReceiverStatus(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType);

    Message findByReceiverMemberNoAndMsgNo(Long memberNo, Long msgNo);

    Page<Message> findBySenderMemberNoAndSenderStatus(Pageable pageable, Long memberNo, SenderStatusType senderStatusType);

    Message findBySenderMemberNoAndMsgNo(Long memberNo, Long msgNo);

    Message findByReceiverMemberNoAndMsgNoAndReceiverStatus(Long memberNo, Long msgNo, ReceiverStatusType receiverStatusType);

    Message findBySenderMemberNoAndMsgNoAndSenderStatus(Long memberNo, Long msgNo, SenderStatusType senderStatusType);


    Page<Message> findByReceiverMemberNoAndReceiverStatusAndSendedAtGreaterThanEqual(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType, LocalDateTime startDateTime);

    Page<Message> findByReceiverMemberNoAndReceiverStatusAndSendedAtLessThanEqual(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType, LocalDateTime endDateTime);

    Page<Message> findByReceiverMemberNoAndReceiverStatusAndSendedAtBetween(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType, LocalDateTime startDateTime, LocalDateTime endDateTime);

    Page<Message> findByReceiverMemberNoAndReceiverStatusAndSenderMemberNameContainingAndSendedAtGreaterThanEqual(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType, String memberName, LocalDateTime startDateTime);

    Page<Message> findByReceiverMemberNoAndReceiverStatusAndSenderMemberNameContainingAndSendedAtLessThanEqual(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType, String memberName, LocalDateTime endDateTime);

    Page<Message> findByReceiverMemberNoAndReceiverStatusAndSenderMemberNameContainingAndSendedAtBetween(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType, String memberName, LocalDateTime startDateTime, LocalDateTime endDateTime);

    Page<Message> findByReceiverMemberNoAndReceiverStatusAndSenderMemberNameContaining(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType, String memberName);

    Page<Message> findByReceiverMemberNoAndReceiverStatusAndMsgContentContaining(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType, String content);

    Page<Message> findByReceiverMemberNoAndReceiverStatusAndMsgContentContainingAndSendedAtGreaterThanEqual(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType, String content, LocalDateTime startDateTime);

    Page<Message> findByReceiverMemberNoAndReceiverStatusAndMsgContentContainingAndSendedAtLessThanEqual(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType, String content, LocalDateTime endDateTime);

    Page<Message> findByReceiverMemberNoAndReceiverStatusAndMsgContentContainingAndSendedAtBetween(Pageable pageable, Long memberNo, ReceiverStatusType receiverStatusType, String content, LocalDateTime startDateTime, LocalDateTime endDateTime);
    
    Page<Message> findBySenderMemberNoAndSenderStatusAndReadAtGreaterThanEqual(Pageable pageable, Long memberNo, SenderStatusType senderStatusType, LocalDateTime startedDateTime);

    Page<Message> findBySenderMemberNoAndSenderStatusAndReadAtLessThanEqual(Pageable pageable, Long memberNo, SenderStatusType senderStatusType, LocalDateTime endedDateTime);


    Page<Message> findBySenderMemberNoAndSenderStatusAndReadAtBetween(Pageable pageable, Long memberNo, SenderStatusType senderStatusType, LocalDateTime startedDateTime, LocalDateTime endedDateTime);
    
    
    Page<Message> findBySenderMemberNoAndSenderStatusAndReceiverMemberNameContaining(Pageable pageable, Long memberNo, SenderStatusType senderStatusType, String memberName);

    Page<Message> findBySenderMemberNoAndSenderStatusAndReceiverMemberNameContainingAndReadAtGreaterThanEqual(Pageable pageable, Long memberNo, SenderStatusType senderStatusType, String memberName, LocalDateTime startedDateTime);

    Page<Message> findBySenderMemberNoAndSenderStatusAndReceiverMemberNameContainingAndReadAtLessThanEqual(Pageable pageable, Long memberNo, SenderStatusType senderStatusType, String memberName, LocalDateTime endedDateTime);

    Page<Message> findBySenderMemberNoAndSenderStatusAndReceiverMemberNameContainingAndReadAtBetween(Pageable pageable, Long memberNo, SenderStatusType senderStatusType, String memberName, LocalDateTime startedDateTime, LocalDateTime endedDateTime);

    Page<Message> findBySenderMemberNoAndSenderStatusAndMsgContentContaining(Pageable pageable, Long memberNo, SenderStatusType senderStatusType, String content);

    Page<Message> findBySenderMemberNoAndSenderStatusAndMsgContentContainingAndReadAtGreaterThanEqual(Pageable pageable, Long memberNo, SenderStatusType senderStatusType, String content, LocalDateTime startedDateTime);

    Page<Message> findBySenderMemberNoAndSenderStatusAndMsgContentContainingAndReadAtLessThanEqual(Pageable pageable, Long memberNo, SenderStatusType senderStatusType, String content, LocalDateTime endedDateTime);

    Page<Message> findBySenderMemberNoAndSenderStatusAndMsgContentContainingAndReadAtBetween(Pageable pageable, Long memberNo, SenderStatusType senderStatusType, String content, LocalDateTime startedDateTime, LocalDateTime endedDateTime);
}

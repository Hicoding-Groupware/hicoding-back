package com.hook.hicodingapi.msg.domain;

import com.hook.hicodingapi.file.domain.File;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.msg.domain.type.ReadStatusType;
import com.hook.hicodingapi.msg.domain.type.ReceiverStatusType;
import com.hook.hicodingapi.msg.domain.type.SenderStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;


import static com.hook.hicodingapi.msg.domain.type.ReadStatusType.NOTREAD;
import static com.hook.hicodingapi.msg.domain.type.ReceiverStatusType.RECEIVER_USABLE;
import static com.hook.hicodingapi.msg.domain.type.SenderStatusType.SENDER_USABLE;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_msg")
@Getter
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long msgNo;

    @Column(nullable = false)
    private String msgContent;

    @CreatedDate
    private LocalDateTime sendedAt;

    private LocalDateTime readAt;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private Member sender;

    @ManyToOne
    @JoinColumn(name = "receiver", nullable = false)
    private Member receiver;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private final SenderStatusType senderStatus = SENDER_USABLE;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private final ReceiverStatusType receiverStatus = RECEIVER_USABLE;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private final ReadStatusType readStatus = NOTREAD;


    public Message(String msgContent, Member sender, Member receiver) {
        this.msgContent = msgContent;
        this.sender = sender;
        this.receiver = receiver;
    }

    public static Message of(String msgContent, Member sender, Member receiver) {

        return new Message(
                msgContent,
                sender,
                receiver
        );
    }
}

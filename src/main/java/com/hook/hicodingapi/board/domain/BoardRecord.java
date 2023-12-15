package com.hook.hicodingapi.board.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hook.hicodingapi.board.domain.type.BoardRecordType;
import com.hook.hicodingapi.member.domain.Member;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "tbl_board_record")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class BoardRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recordNo;

    @NotNull
    @Enumerated(value = STRING)
    private BoardRecordType recordType;

    @CreatedDate
    @Column(nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "memberNo")
    private Member recorder;

    @NotNull
    private Long postNo;

    private BoardRecord(BoardRecordType recordType, Member recorder, Long postNo) {
        this.recordType = recordType;
        this.recorder = recorder;
        this.postNo = postNo;
    }

    public static Optional<BoardRecord> of(final BoardRecordType boardRecordType,
                                           final Member recorder,
                                           final Long postNo) {
        return Optional.of(new BoardRecord(boardRecordType, recorder, postNo));
    }
}

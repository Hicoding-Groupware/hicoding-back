package com.hook.hicodingapi.lecture.domain;

import com.hook.hicodingapi.lecture.domain.type.LectureStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.hook.hicodingapi.lecture.domain.type.LectureStatusType.AVAILABLE;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_lecture")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Lecture {

    @Id
    private Long lecCode;

    @Column(nullable = false)
    private String lecName;

    @Column(nullable = false)
    private String textbook;

    @Column(nullable = false)
    private String techStack;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private LectureStatusType status = AVAILABLE;
}

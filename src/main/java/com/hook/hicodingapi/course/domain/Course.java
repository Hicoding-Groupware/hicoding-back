package com.hook.hicodingapi.course.domain;

import com.hook.hicodingapi.classroom.Classroom;
import com.hook.hicodingapi.course.domain.type.CourseStatusType;
import com.hook.hicodingapi.course.domain.type.DayStatusType;
import com.hook.hicodingapi.course.domain.type.TimeStatusType;
import com.hook.hicodingapi.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.hook.hicodingapi.course.domain.type.CourseStatusType.AVAILABLE;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_course")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Course {

    @Id
    private Long cosCode;

    @Column(nullable = false)
    private LocalDate cosSdt;

    @Column(nullable = false)
    private LocalDate cosEdt;

    @Column(nullable = false)
    private Long capacity;

    @ManyToOne
    @JoinColumn(name = "roomCode")
    private Classroom classroom;

    @Column(nullable = false)
    private String cosName;

    @Column(nullable = false)
    private Long curCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher")
    private Member member;

    @Column(nullable = false)
    private Long staff;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private DayStatusType dayStatus;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private TimeStatusType timeStatus;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedAt;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private CourseStatusType status = AVAILABLE;







}

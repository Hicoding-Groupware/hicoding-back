package com.hook.hicodingapi.course.domain;

import com.hook.hicodingapi.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_course")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class) // 엔터티에 대한 변경 사항을 추적하고 기록, 생성일자, 수정일자 자동으로 관리할 때 유용
public class Course {

    @Id
//    @GeneratedValue(strategy = IDENTITY)  데이터 베이스 자동 증가 컬럼 활용
    private Long cosCode;

    @Column(nullable = false)
    private LocalDate cosSdt;

    @Column(nullable = false)
    private LocalDate cosEdt;

    @Column(nullable = false)
    private LocalTime claSt;

    @Column(nullable = false)
    private LocalTime claEt;

    @Column(nullable = false)
    private Long capacity;

    @Column(nullable = false)
    private Long roomCode;

    @Column(nullable = false)
    private Long lecCode;

    @Column(nullable = false)
    private String cosName;

    @Column(nullable = false)
    private Long curCnt;

    @Column(nullable = false)
    private Long teacher;

    @Column(nullable = false)
    private Long staff;

    @Column(nullable = false)
    private String dayStatus;

    @Column(nullable = false)
    private String timeStatus;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    @Column(nullable = false)
    private Long registStaff;

    @Column(nullable = false)
    private String Status;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "memberNo")
    @JoinColumn(name = "teacher", insertable = false, updatable = false)
    private Member member;





}

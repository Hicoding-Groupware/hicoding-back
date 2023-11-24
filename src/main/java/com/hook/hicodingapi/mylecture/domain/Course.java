package com.hook.hicodingapi.mylecture.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_course")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Course {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long cosCode;

    @Column(nullable = false)
    private String cosName;

    @Column(nullable = false)
    private Long teacher;

    @Column(nullable = false)
    private LocalTime claSt;

    @Column(nullable = false)
    private LocalTime claEt;

    @Column(nullable = false)
    private String dayStatus;

    @Column(nullable = false) // int?
    private Long curCnt;

    @Column(nullable = false)
    private LocalDate cosSdt;

    @Column(nullable = false)
    private LocalDate cosEdt;


}

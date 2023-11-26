package com.hook.hicodingapi.student.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_student")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Student {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long stdCode;

    @Column(nullable = false)
    private String stdName;

    @Column(nullable = false)
    private String stdGender;

    @Column(nullable = false)
    private Date stdBirth;

    @Column(nullable = false)
    private String stdPhone;

    @Column
    private String stdEmail;

    @Column
    private String postNo;

    @Column
    private String address;

    @Column
    private String detailAddress;

    @Column
    private String stdMemo;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public Student(String stdName, String stdGender, Date stdBirth, String stdPhone, String stdEmail, String postNo, String address, String detailAddress, String stdMemo) {
        this.stdName = stdName;
        this.stdGender = stdGender;
        this.stdBirth = stdBirth;
        this.stdPhone = stdPhone;
        this.stdEmail = stdEmail;
        this.postNo = postNo;
        this.address = address;
        this.detailAddress = detailAddress;
        this.stdMemo = stdMemo;
    }

    public static Student of(final String stdName, final String stdGender, final Date stdBirth,
                             final String stdPhone, final String stdEmail, final String postNo,
                             final String address, final String detailAddress, final String stdMemo) {

        return new Student(
                stdName,
                stdGender,
                stdBirth,
                stdPhone,
                stdEmail,
                postNo,
                address,
                detailAddress,
                stdMemo
        );
    }
}

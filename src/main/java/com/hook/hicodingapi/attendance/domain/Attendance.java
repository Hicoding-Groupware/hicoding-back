package com.hook.hicodingapi.attendance.domain;


import com.hook.hicodingapi.attendance.domain.type.AttendanceStatusType;
import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_attendance")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Attendance {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long atdCode;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate atdDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AttendanceStatusType atdStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stdCode")
    private Student stdCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosCode")
    private Course cosCode;

    public Attendance(Student student, AttendanceStatusType status, Course cosCode) {
        this.stdCode = student;
        this.atdStatus = status;
        this.cosCode = cosCode;
    }


    public static Attendance of(Student student, AttendanceStatusType status, Course cosCode) {

        return new Attendance(
                student,
                status,
                cosCode
        );
    }


    public void update(LocalDate atdDate, Course cosCode, Student stdCode, Long atdCode, AttendanceStatusType atdStatus) {
        this.atdDate = atdDate;
        this.cosCode = cosCode;
        this.stdCode = stdCode;
        this.atdCode = atdCode;
        this.atdStatus = atdStatus;

    }
}

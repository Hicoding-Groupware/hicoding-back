package com.hook.hicodingapi.record.domain;

import com.hook.hicodingapi.course.domain.Course;
import com.hook.hicodingapi.record.domain.type.SignupStatusType;
import com.hook.hicodingapi.student.domain.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.hook.hicodingapi.record.domain.type.SignupStatusType.NORMAL;
import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "tbl_record")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recCode;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private SignupStatusType signupStatus = NORMAL;

    @Column
    private LocalDateTime withdrawDate;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "stdCode")
//    private Student student;
    private Long stdCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosCode")
    private Course course;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime registedDate;

    public Record(Long stdCode, Course course) {

        this.stdCode = stdCode;
        this.course = course;
    }


    public static Record of(final Long stdCode, final Course course) {
        return new Record(
                stdCode,
                course
        );
    }


    public void withdraw() {
        this.signupStatus = SignupStatusType.WITHDRAW;
        this.withdrawDate = LocalDateTime.now();
    }
}

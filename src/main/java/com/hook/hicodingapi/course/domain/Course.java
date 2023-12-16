package com.hook.hicodingapi.course.domain;

import com.hook.hicodingapi.attendance.domain.Attendance;
import com.hook.hicodingapi.classroom.domain.Classroom;
import com.hook.hicodingapi.course.domain.type.CourseStatusType;
import com.hook.hicodingapi.course.domain.type.DayStatusType;
import com.hook.hicodingapi.course.domain.type.TimeStatusType;
import com.hook.hicodingapi.lecture.domain.Lecture;
import com.hook.hicodingapi.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.hook.hicodingapi.course.domain.type.CourseStatusType.AVAILABLE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_course")
@NoArgsConstructor(access = PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update tbl_course set status = 'DELETED' where cos_code = ?")
public class Course {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long cosCode;

    @Column(nullable = false)
    private String cosName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecCode")
    private Lecture lecCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher")
    private Member teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff")
    private Member staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomCode")
    private Classroom classroom;

    @Column(nullable = false)
    private LocalDate cosSdt;

    @Column(nullable = false)
    private LocalDate cosEdt;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private int curCnt = 0;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private DayStatusType dayStatus;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private TimeStatusType timeStatus;

    @Column
    private String cosNotice;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private CourseStatusType status = AVAILABLE;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedAt;

    @OneToMany
    @JoinColumn(name = "cosCode")
    private List<Attendance> attendCosCode;

    public Course(String cosName, Lecture lecture, Member teacher, Member staff, Classroom classroom, LocalDate cosSdt,
                  LocalDate cosEdt, int capacity, String cosNotice, DayStatusType dayStatus, TimeStatusType timeStatus) {
        this.cosName = cosName;
        this.lecCode = lecture;
        this.teacher = teacher;
        this.staff = staff;
        this.classroom = classroom;
        this.cosSdt = cosSdt;
        this.cosEdt = cosEdt;
        this.capacity = capacity;
        this.cosNotice = cosNotice;
        this.dayStatus = dayStatus;
        this.timeStatus = timeStatus;
    }
    public void updateCurCnt ( int curCnt){

        this.curCnt += 1;
    }

    public void downCurcnt ( int curCnt){

        this.curCnt -= 1;
    }


    public static Course of(
            final String cosName, final Lecture lecture, final Member teacher, final Member staff, final Classroom classroom,
            final LocalDate cosSdt, final LocalDate cosEdt, final int capacity, final String cosNotice, final DayStatusType dayStatus,
            final TimeStatusType timeStatus) {


            return new Course(
                    cosName,
                    lecture,
                    teacher,
                    staff,
                    classroom,
                    cosSdt,
                    cosEdt,
                    capacity,
                    cosNotice,
                    dayStatus,
                    timeStatus
            );
        }

        public void update (String cosName, Lecture lecture, Member teacher, Member staff, Classroom classroom,
                LocalDate cosSdt, LocalDate cosEdt,int capacity, int curCnt, DayStatusType dayStatus,
                TimeStatusType timeStatus, String cosNotice){
            this.cosName = cosName;
            this.lecCode = lecture;
            this.teacher = teacher;
            this.staff = staff;
            this.classroom = classroom;
            this.cosSdt = cosSdt;
            this.cosEdt = cosEdt;
            this.capacity = capacity;
            this.curCnt = curCnt;
            this.dayStatus = dayStatus;
            this.timeStatus = timeStatus;
            this.cosNotice = cosNotice;
    }



    }


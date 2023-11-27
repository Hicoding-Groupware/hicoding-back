package com.hook.hicodingapi.lecture.domain;

import com.hook.hicodingapi.lecture.domain.type.LectureStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.hook.hicodingapi.lecture.domain.type.LectureStatusType.AVAILABLE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_lecture")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "update tbl_lecture set status = 'DELETED' where lec_code = ?")
public class Lecture {

    @Id
    @GeneratedValue(strategy = IDENTITY)
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

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private LectureStatusType status = AVAILABLE;

    public Lecture(String lecName, String textbook, String techStack) {
        this.lecName = lecName;
        this.textbook = textbook;
        this.techStack = techStack;
    }

    public static Lecture of(
            final String lecName, final String textbook, final String techStack) {

        return new Lecture(
                lecName,
                textbook,
                techStack
        );
    }

    public void update(String lecName, String textbook, String techStack) {
        this.lecName = lecName;
        this.textbook = textbook;
        this.techStack = techStack;
    }
}

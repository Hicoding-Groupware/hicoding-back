package com.hook.hicodingapi.student.domain;

import com.hook.hicodingapi.student.domain.type.SignupStatusType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.hook.hicodingapi.student.domain.type.SignupStatusType.수강중;
import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "tbl_record")
@NoArgsConstructor
@Getter
public class Record {

    @Id
    private Long recCode;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private SignupStatusType status = 수강중;
}

package com.hook.hicodingapi.classroom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_classroom")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Classroom {

    @Id
    private Long roomCode;

    @Column(nullable = false)
    private String roomName;
}

package com.hook.hicodingapi.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_member")
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Member {

    @Id
    private Long memberNo;

//    @OneToMany(mappedBy = "member")
//    private List<Course> course;

    @Column(nullable = false)
    private String memberName;

}

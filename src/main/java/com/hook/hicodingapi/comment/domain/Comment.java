package com.hook.hicodingapi.comment.domain;

import com.hook.hicodingapi.board.domain.Board;
import com.hook.hicodingapi.common.domain.BaseEntity;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "tbl_comment")
@NoArgsConstructor
@Getter
@ToString
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cmtNo;

    @NotNull
    private String cmtContent;

    @NotNull
    @Enumerated(value = STRING)
    private StatusType status;

    @ManyToOne
    @JoinColumn(name = "postNo")
    private Board postNo;

    @ManyToOne
    @JoinColumn(name = "memberNo")
    private Member memberNo;

    @ManyToOne
    @JoinColumn(name = "refCmtNo")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}

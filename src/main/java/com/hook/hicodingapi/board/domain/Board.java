package com.hook.hicodingapi.board.domain;

import com.hook.hicodingapi.comment.domain.Comment;
import com.hook.hicodingapi.common.domain.BaseEntity;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "tbl_post")
@NoArgsConstructor
@Getter
@ToString
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    @NotNull
    private String postTitle;

    @NotNull
    private String postContent;

    @NotNull
    @Enumerated(value = STRING)
    private MemberRole postRole;

    @NotNull
    @Min(0)
    private Integer views;

    @NotNull
    @Min(0)
    private Integer likesCount;

    @NotNull
    @Enumerated(value = STRING)
    private StatusType status;

    @NotNull
    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "memberNo")
    private Member memberNo;

    @ManyToOne
    @JoinColumn(name = "refPostNo")
    private Board parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> replies = new ArrayList<>();

    @OneToMany(mappedBy = "postNo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}
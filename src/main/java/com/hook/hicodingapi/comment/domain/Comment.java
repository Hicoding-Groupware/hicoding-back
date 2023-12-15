package com.hook.hicodingapi.comment.domain;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.common.domain.BaseEntity;
import com.hook.hicodingapi.member.domain.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "tbl_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cmtNo;

    @NotNull
    private String cmtContent;

    @ManyToOne
    @JoinColumn(name = "postNo")
    private Post postCode;

    @ManyToOne
    @JoinColumn(name = "memberNo")
    private Member writer;

    @ManyToOne
    @JoinColumn(name = "refCmtNo")
    private Comment parent;

    // cascade = CascadeType.ALL, orphanRemoval = true
    @OneToMany(mappedBy = "parent")
    private List<Comment> childrenList;

    private Comment(final String content) {
        this.cmtContent = content;
    }

    public static Optional<Comment> of(final String content) {
        return Optional.of(new Comment(content));
    }
}

package com.hook.hicodingapi.board.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hook.hicodingapi.board.domain.type.BoardType;
import com.hook.hicodingapi.board.dto.request.PostCreationRequest;
import com.hook.hicodingapi.board.dto.request.PostUpdateRequest;
import com.hook.hicodingapi.comment.domain.Comment;
import com.hook.hicodingapi.common.domain.BaseEntity;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;

import static com.hook.hicodingapi.common.domain.type.StatusType.USABLE;
import static javax.persistence.EnumType.STRING;

@Entity
@Table(name = "tbl_post")
@NoArgsConstructor
@Getter
@Setter
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postNo;

    @NotNull
    private String postTitle;

    @NotNull
    private String postContent;

    @NotNull
    @Enumerated(value = STRING)
    private BoardType boardType;

    @NotNull
    @Min(0)
    private Integer views = 0;

    @NotNull
    @Min(0)
    private Integer likesCount = 0;

    @NotNull
    @Enumerated(value = STRING)
    private StatusType status = USABLE;

    @NotNull
    private boolean isPublic;

    @NotNull
    private boolean isNoticePost;

    @ManyToOne
    @JoinColumn(name = "memberNo")
    private Member writer;

    @ManyToOne
    @JoinColumn(name = "refPostNo")
    private Post parent;

    @OneToMany(mappedBy = "parent")
    private List<Post> childrenList;

    @OneToMany(mappedBy = "postNo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList;

    private Post(String title, String content, BoardType boardType, boolean isPublic, boolean isNoticePost) {
        this.postTitle = title;
        this.postContent = content;
        this.boardType = boardType;
        this.isPublic = isPublic;
        this.isNoticePost = isNoticePost;
    }

    public static Optional<Post> of(final BoardType boardType,
                                    final PostCreationRequest postCreationRequest) {
        return Optional.of(
                new Post(
                        postCreationRequest.getTitle(),
                        postCreationRequest.getContent(),
                        boardType,
                        postCreationRequest.isPublic(),
                        postCreationRequest.isNoticePost()
                )
        );
    }

    public void update(PostUpdateRequest postUpdateRequest) {
        this.postTitle = postUpdateRequest.getTitle();
        this.postContent = postUpdateRequest.getContent();
        this.isPublic = postUpdateRequest.isPublic();
        this.isNoticePost = postUpdateRequest.isNoticePost();
    }
}
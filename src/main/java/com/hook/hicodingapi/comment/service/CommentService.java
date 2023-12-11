package com.hook.hicodingapi.comment.service;

import com.hook.hicodingapi.board.domain.repository.BoardRepository;
import com.hook.hicodingapi.comment.domain.Comment;
import com.hook.hicodingapi.comment.domain.repository.CommentRepository;
import com.hook.hicodingapi.comment.domain.type.CommentCriteriaConditionType;
import com.hook.hicodingapi.comment.dto.request.CommentCreationRequest;
import com.hook.hicodingapi.comment.domain.repository.CommentCriteriaRepository;
import com.hook.hicodingapi.comment.dto.request.CommentEditRequest;
import com.hook.hicodingapi.comment.dto.response.CommentReadResponse;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.common.exception.CustomException;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final CommentCriteriaRepository commentCriteriaRepository;

    // 게시글의 댓글 전체 조회
    @Transactional(readOnly = true)
    public List<Comment> findAllCommentsOfPost(final Long postNo) {

        final List<Comment> findCommentList = commentCriteriaRepository.getPostListByCondition(
                CommentCriteriaConditionType.ALL_COMMENT, postNo
        ).orElseThrow(() -> new CustomException(NOT_FOUND_COMMENTS_CODE));

        return findCommentList;
    }

    // 댓글 조회
    public Comment findComment(final Long commentNo) {
        final Comment readComment = commentRepository.findCommentByCmtNoAndStatus(commentNo, StatusType.USABLE)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT_CODE));

        return readComment;
    }

    // 댓글 생성
    public Comment createComment(final CommentCreationRequest commentCreationRequest) {

        // 댓글 생성
        final Comment newComment = Comment.of(commentCreationRequest.getContent())
                .orElseThrow(() -> new CustomException(FAIL_CREATION_COMMENT_CODE));

        // 현재 게시글에 연동
        newComment.setPostCode(
                boardRepository.findByPostNoAndStatus(commentCreationRequest.getPostNo(), StatusType.USABLE)
                        .orElseThrow(() -> new CustomException(NOT_FOUND_POST_CODE))
        );

        // 댓글 작성자
        newComment.setWriter(
                memberRepository.findByMemberNo(commentCreationRequest.getWriterNo())
                        .orElseThrow(() -> new CustomException(NOT_FOUND_WRITER_CODE))
        );

        final Comment parentComment = commentRepository.findCommentByCmtNoAndStatus(
                commentCreationRequest.getParentNo(), StatusType.USABLE
                ).orElse(null);
        
        // 부모 댓글이 존재
        if (parentComment != null) {
            // 부모 게시글이 생성 대댓글 게시글과 일치하지 않는다면 문제 
            if (parentComment.getPostCode().getPostNo() != commentCreationRequest.getPostNo())
                throw new CustomException(CONFLICT_PARENT_AND_CHILD_COMMENT_CODE);

            // 부모 댓글로 지정
            newComment.setParent(parentComment);
        }
        
        commentRepository.save(newComment);

        return newComment;
    }

    // 댓글 수정
    public Comment updateComment(final Long commentNo, final CommentEditRequest commentEditRequest) {
        final Comment editComment = commentRepository.findCommentByCmtNoAndStatus(commentNo, StatusType.USABLE)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT_CODE));

        editComment.setCmtContent(commentEditRequest.getContent());
        editComment.setStatus(commentEditRequest.getStatus());

        return editComment;
    }

    // 댓글 삭제
    public void deleteComment(final Long cmtNo) {
        final Comment deletionComment = commentRepository.findCommentByCmtNoAndStatus(cmtNo, StatusType.USABLE)
                .orElseThrow(() -> new CustomException(NOT_FOUND_COMMENT_CODE));

        deletionComment.setStatus(StatusType.DELETED);

        // 서브 자식이 있다면, 제목 앞에 [원 댓글이 삭제된 댓글]을 붙이게 한다.
        for (Comment childComment : deletionComment.getChildrenList()) {
            childComment.setCmtContent("[원글이 삭제된 답글] " + childComment.getCmtContent());
        }
    }

    // 게시글의 대댓글을 가진 댓글들을 구조 분해시켜 응답 DTO에 담는다.
    public List<CommentReadResponse> convertHierarchicalCommentList(final List<Comment> commentList) {

        // 대댓글이 없는 댓글(부모)은 List에 담고, 대댓글은 부모를 불러(체인) 연결하는 계층 알고리즘
        List<CommentReadResponse> commentReadResponseList = new ArrayList<>();
        Map<Long, CommentReadResponse> commentReadResponseHashMap = new HashMap<>();

        commentList.forEach(comment -> {
            CommentReadResponse currCommentReadResponse = CommentReadResponse.from(comment);
            commentReadResponseHashMap.put(currCommentReadResponse.getNo(), currCommentReadResponse);

            if (comment.getParent() != null) {
                CommentReadResponse parent = commentReadResponseHashMap.get(comment.getParent().getCmtNo());

                // 게시글 답글들이 있다면
                if (parent.getChildrenList() == null)
                    parent.setChildrenList(new ArrayList<>());

                parent.getChildrenList().add(currCommentReadResponse);
            } else {
                commentReadResponseList.add(currCommentReadResponse);
            }
        });

        return commentReadResponseList;
    }
}

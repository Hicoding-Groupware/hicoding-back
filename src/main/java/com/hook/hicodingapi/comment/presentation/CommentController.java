package com.hook.hicodingapi.comment.presentation;

import com.hook.hicodingapi.comment.domain.Comment;
import com.hook.hicodingapi.comment.dto.request.CommentCreationRequest;
import com.hook.hicodingapi.comment.dto.request.CommentEditRequest;
import com.hook.hicodingapi.comment.dto.response.CommentCreationResponse;
import com.hook.hicodingapi.comment.dto.response.CommentReadResponse;
import com.hook.hicodingapi.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hook.hicodingapi.common.ApiURIConstants.*;

@RestController
@RequestMapping(BASE_PATH + COMMENT_PATH)
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 게시글의 댓글 전체 조회
    @GetMapping("/post/{postNo}")
    public ResponseEntity<List<CommentReadResponse>> getRequestedAllComments(@PathVariable final Long postNo) {

        final List<Comment> findCommentList = commentService.findAllCommentsOfPost(postNo);
        final List<CommentReadResponse> commentReadResponseList = commentService.convertHierarchicalCommentList(findCommentList);
        return ResponseEntity.ok(commentReadResponseList);
    }

    // 댓글 조회
    @GetMapping("/{commentNo}")
    public ResponseEntity<CommentReadResponse> getRequestedComment(@PathVariable final Long commentNo) {

        final Comment findComment = commentService.findComment(commentNo);
        final CommentReadResponse commentReadResponse = CommentReadResponse.from(findComment);
        return ResponseEntity.ok(commentReadResponse);
    }

    // 댓글 등록
    @PostMapping()
    public ResponseEntity<CommentCreationResponse> saveRequestedPost(@RequestBody final CommentCreationRequest commentCreationRequest) {

        final Comment createdComment = commentService.createComment(commentCreationRequest);
        final CommentCreationResponse commentCreationResponse = CommentCreationResponse.from(createdComment);

        return ResponseEntity.ok(commentCreationResponse);
    }

    // 댓글 수정
    @PutMapping("/{commentNo}")
    public ResponseEntity<CommentReadResponse> updateRequestedComment(@PathVariable final Long commentNo, @RequestBody final CommentEditRequest commentEditRequest) {

        final Comment updatedComment = commentService.updateComment(commentNo, commentEditRequest);
        final CommentReadResponse commentReadResponse = CommentReadResponse.from(updatedComment);
        return ResponseEntity.ok(commentReadResponse);
    }

    // 댓글 삭제
    @DeleteMapping("/{commentNo}")
    public ResponseEntity<Void> deleteRequestedComment(@PathVariable final Long commentNo) {

        commentService.deleteComment(commentNo);

        return ResponseEntity.noContent().build();
    }
}

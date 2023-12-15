package com.hook.hicodingapi.comment.presentation;

import com.hook.hicodingapi.comment.domain.Comment;
import com.hook.hicodingapi.comment.dto.request.CommentCreationRequest;
import com.hook.hicodingapi.comment.dto.request.CommentEditRequest;
import com.hook.hicodingapi.comment.dto.response.CommentCreationResponse;
import com.hook.hicodingapi.comment.dto.response.CommentReadResponse;
import com.hook.hicodingapi.comment.service.CommentService;
import com.hook.hicodingapi.common.paging.CustomPagination;
import com.hook.hicodingapi.common.paging.PagingResponse;
import lombok.AccessLevel;
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
    @GetMapping("/post/{postNo}/{requestPage}")
    public ResponseEntity<PagingResponse> getRequestedAllComments(@PathVariable(required = true) final Long postNo,
                                                                  @PathVariable(required = true) final int requestPage) {

        final List<Comment> findCommentList = commentService.findAllCommentsOfPost(postNo);
        final List<CommentReadResponse> commentReadResponseList = commentService.convertHierarchicalCommentList(findCommentList);
        final PagingResponse pagingResponse = CustomPagination.getPagingResponse(commentReadResponseList, commentReadResponseList.size(), requestPage, null, null);

        return ResponseEntity.ok(pagingResponse);
    }

    // 댓글 조회
    @GetMapping("/{commentNo}")
    public ResponseEntity<CommentReadResponse> getRequestedComment(@PathVariable(required = true) final Long commentNo) {

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

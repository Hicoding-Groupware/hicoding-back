package com.hook.hicodingapi.board.presentation;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.board.domain.type.BoardType;
import com.hook.hicodingapi.board.dto.request.PostCreationRequest;
import com.hook.hicodingapi.board.dto.request.PostEditRequest;
import com.hook.hicodingapi.board.dto.response.PostCreationResponse;
import com.hook.hicodingapi.board.dto.response.PostReadResponse;
import com.hook.hicodingapi.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.hook.hicodingapi.common.ApiURIConstants.*;

@RestController
@RequestMapping(BASE_PATH + BOARD_PATH)
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 전체 가져오기
    @GetMapping("/{boardType}/post")
    public ResponseEntity<List<PostReadResponse>> getRequestedAllPosts(@PathVariable final String boardType) {

        final BoardType convertedBoardType = BoardType.fromValue(boardType);

        final List<Post> findPostList = boardService.findBoardAllPosts(convertedBoardType);
        final List<PostReadResponse> postReadResponseList = boardService.convertHierarchicalPostList(findPostList);
        return ResponseEntity.ok(postReadResponseList);
    }

    // 게시글 조회
    @GetMapping("/{boardType}/post/{postNo}")
    public ResponseEntity<PostReadResponse> getRequestedPost(@PathVariable final String boardType, @PathVariable final Long postNo) {

        final BoardType convertedBoardType = BoardType.fromValue(boardType);
        final Post findPost = boardService.findPost(convertedBoardType, postNo);
        final PostReadResponse postReadResponse = PostReadResponse.from(findPost);

        // 게시글의 자식들을 응답 객체의 자식들로 변환시킨다.
        boardService.convertObjChildrenToReadResponseObjChildren(postReadResponse, findPost.getChildrenList(), findPost.getCommentList());

        final URI location = URI.create(BASE_PATH + BOARD_PATH + +'/' + boardType + "/post" + '/' + postNo);
        return ResponseEntity.created(location).body(postReadResponse);
    }

    // 게시글 등록
    @PostMapping("/{boardType}/post")
    public ResponseEntity<PostCreationResponse> saveRequestedPost(@PathVariable final String boardType, @RequestBody final PostCreationRequest postCreationReq) {
        final BoardType convertedBoardType = BoardType.fromValue(boardType);
        final PostCreationResponse creationResponse = PostCreationResponse.from(
                boardService.createPost(convertedBoardType, postCreationReq
                ));
        return ResponseEntity.ok(creationResponse);
    }

    // 게시글 수정
    @PutMapping("/{boardType}/post/{postNo}")
    public ResponseEntity<PostReadResponse> updateRequestedPost(@PathVariable final String boardType,
                                                    @PathVariable final Long postNo,
                                                    @RequestBody final PostEditRequest postEditRequest) {
        final BoardType convertedBoardType = BoardType.fromValue(boardType);
        final Post updatedPost = boardService.updatePost(convertedBoardType, postNo, postEditRequest);
        final PostReadResponse postReadResponse = PostReadResponse.from(updatedPost);

        // 응답 객체에 자식들을 이어붙인다.
        boardService.convertObjChildrenToReadResponseObjChildren(postReadResponse, updatedPost.getChildrenList(), updatedPost.getCommentList());

        final URI location = URI.create(BASE_PATH + BOARD_PATH + +'/' + boardType + "/post" + '/' + postNo);
        return ResponseEntity.created(location).body(postReadResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardType}/post/{postNo}")
    public ResponseEntity<Void> deleteRequestedPost(@PathVariable final String boardType, @PathVariable final Long postNo) {

        final BoardType convertedBoardType = BoardType.fromValue(boardType);
        boardService.deletePost(convertedBoardType, postNo);

        return ResponseEntity.noContent().build();
    }
}

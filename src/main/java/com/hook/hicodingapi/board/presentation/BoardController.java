package com.hook.hicodingapi.board.presentation;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.board.domain.type.BoardRecordType;
import com.hook.hicodingapi.board.domain.type.BoardType;
import com.hook.hicodingapi.board.dto.request.PostCreationRequest;
import com.hook.hicodingapi.board.dto.request.PostEditRequest;
import com.hook.hicodingapi.board.dto.response.LikesPostResponse;
import com.hook.hicodingapi.board.dto.response.PostCreationResponse;
import com.hook.hicodingapi.board.dto.response.PostReadResponse;
import com.hook.hicodingapi.board.service.BoardService;
import com.hook.hicodingapi.common.paging.CustomPagination;
import com.hook.hicodingapi.common.paging.PagingResponse;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.dto.response.PostMembersResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.hook.hicodingapi.common.ApiURIConstants.*;

@RestController
@RequestMapping(BASE_PATH + BOARD_PATH)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardController {

    private final BoardService boardService;

    // 게시글 전체 가져오기
    @GetMapping("/{boardType}/posts/{requestPage}")
    public ResponseEntity<PagingResponse> getRequestedAllPosts(@PathVariable(required = true) final String boardType,
                                                                       @PathVariable(required = true) final int requestPage) {

        final List<Post> findPostList = boardService.findBoardAllPosts(BoardType.fromValue(boardType));
        final List<PostReadResponse> postReadResponseList = boardService.convertHierarchicalPostList(findPostList);
        final PagingResponse pagingResponse = CustomPagination.getPagingResponse(postReadResponseList, postReadResponseList.size(), requestPage, null, null);

        return ResponseEntity.ok(pagingResponse);
    }

    // 게시글 조회
    @GetMapping("/{boardType}/post/{postNo}")
    public ResponseEntity<PostReadResponse> getRequestedPost(@PathVariable final String boardType,
                                                             @PathVariable final Long postNo,
                                                             final Long memberNo,
                                                             final String boardRecordType) {

        final Post findPost = boardService.findPost(BoardType.fromValue(boardType), BoardRecordType.fromValue(boardRecordType), memberNo, postNo);
        final PostReadResponse postReadResponse = PostReadResponse.from(findPost);

        // 게시글의 자식들을 응답 객체의 자식들로 변환시킨다.
        boardService.convertObjChildrenToReadResponseObjChildren(postReadResponse, findPost.getChildrenList(), findPost.getCommentList());

        final URI location = URI.create(BASE_PATH + BOARD_PATH + +'/' + boardType + "/post" + '/' + postNo);
        return ResponseEntity.created(location).body(postReadResponse);
    }

    // 게시글 사용자들 조회
    @GetMapping("/{boardType}/post/{postNo}/record")
    public ResponseEntity<List<PostMembersResponse>> getRequestedPostMembersInfo(@PathVariable final String boardType,
                                                                                 @PathVariable final Long postNo,
                                                                                 final String boardRecordType) {

        final List<Member> memberList = boardService.readPostMembersInfo(postNo, BoardRecordType.fromValue(boardRecordType));
        List<PostMembersResponse> postMembersResponseList = new ArrayList<>(memberList.size());

        memberList.forEach( member -> {postMembersResponseList.add(PostMembersResponse.from(member));});

        return ResponseEntity.ok(postMembersResponseList);
    }

    // 게시글 등록
    @PostMapping("/{boardType}/post")
    public ResponseEntity<PostCreationResponse> saveRequestedPost(@PathVariable final String boardType, @RequestBody final PostCreationRequest postCreationReq) {

        final PostCreationResponse creationResponse = PostCreationResponse.from(
                boardService.createPost(BoardType.fromValue(boardType), postCreationReq
                ));
        return ResponseEntity.ok(creationResponse);
    }

    // 게시글 수정
    @PutMapping("/{boardType}/post/{postNo}")
    public ResponseEntity<PostReadResponse> updateRequestedPost(@PathVariable final String boardType,
                                                                @PathVariable final Long postNo,
                                                                @RequestBody final PostEditRequest postEditRequest) {

        final Post updatedPost = boardService.updatePost(BoardType.fromValue(boardType), postNo, postEditRequest);
        final PostReadResponse postReadResponse = PostReadResponse.from(updatedPost);

        // 응답 객체에 자식들을 이어붙인다.
        boardService.convertObjChildrenToReadResponseObjChildren(postReadResponse, updatedPost.getChildrenList(), updatedPost.getCommentList());

        final URI location = URI.create(BASE_PATH + BOARD_PATH + +'/' + boardType + "/post" + '/' + postNo);
        return ResponseEntity.created(location).body(postReadResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/{boardType}/post/{postNo}")
    public ResponseEntity<Void> deleteRequestedPost(@PathVariable final String boardType, @PathVariable final Long postNo) {

        boardService.deletePost(BoardType.fromValue(boardType), postNo);

        return ResponseEntity.noContent().build();
    }
}

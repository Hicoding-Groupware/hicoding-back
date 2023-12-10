package com.hook.hicodingapi.board.service;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.board.domain.repository.BoardCriteriaRepository;
import com.hook.hicodingapi.board.domain.repository.BoardRepository;
import com.hook.hicodingapi.board.domain.type.BoardCriteriaConditionType;
import com.hook.hicodingapi.board.domain.type.BoardType;
import com.hook.hicodingapi.board.dto.request.PostCreationRequest;
import com.hook.hicodingapi.board.dto.request.PostUpdateRequest;
import com.hook.hicodingapi.board.dto.response.PostReadResponse;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardCriteriaRepository boardCriteriaRepository;

    // 답글을 가진 게시글들을 구조 분해시켜 응답 DTO에 담는다.
    public List<PostReadResponse> convertHierarchicalPostList(final List<Post> postList) {

        // 답글이 없는 게시글(부모)는 List에 담고, 답글 게시글은 부모를 불러 연결(체인)하는 알고리즘
        List<PostReadResponse> postReadResponseList = new ArrayList<>();
        Map<Long, PostReadResponse> postReadResponseHashMap = new HashMap<>();

        postList.forEach(currPost -> {
            PostReadResponse currPostReadResponse = PostReadResponse.from(currPost);
            postReadResponseHashMap.put(currPostReadResponse.getNo(), currPostReadResponse);

            if (currPost.getParent() != null) {
                PostReadResponse parent = postReadResponseHashMap.get(currPost.getParent().getPostNo());
                if (parent.getChildrenList() == null)
                    parent.setChildrenList(new ArrayList<>());

                parent.getChildrenList().add(currPostReadResponse);
            }
            else
                postReadResponseList.add(currPostReadResponse);
        });

        return postReadResponseList;
    }

    @Transactional(readOnly = true)
    // 게시글 전체 조회
    public List<Post> findAllPost(BoardType convertedBoardType) {
        final List<Post> findPostList = boardCriteriaRepository.getPostListByCondition(BoardCriteriaConditionType.FIND_ALL_POST, convertedBoardType).
                orElseThrow(() -> new NotFoundException(NOT_FOUND_POSTS_CODE));

        return findPostList;
    }

    @Transactional(readOnly = true)
    // 게시글 조회
    public Post findPost(final BoardType boardType, final Long postNo) {
        final Post findPost = boardRepository.findByPostNoAndBoardTypeAndStatus(postNo, boardType, StatusType.USABLE)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POST_CODE));

        return findPost;
    }

    // 게시글 등록
    public Post createPost(final BoardType boardType, final PostCreationRequest postCreationReq) {

        final Post newPost = Post.of(boardType, postCreationReq)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CREATION_CODE));

        newPost.setWriter(
                memberRepository.findByMemberNo(postCreationReq.getWriterNo())
                        .orElseThrow(() -> new NotFoundException(NOT_FOUND_WRITER_CODE))
        );

        // parent가 있다는 것은 현재 요청된 게시글이 답글 게시글이라는 것
        final Long parentNo = postCreationReq.getParentNo();
        newPost.setParent(
                parentNo == null ?
                        null : boardRepository.findById(postCreationReq.getParentNo())
                        .orElse(null)
        );

        boardRepository.save(newPost);
        return newPost;
    }

    // 게시글 수정
    public void updatePost(final BoardType boardType, final Long postNo, final PostUpdateRequest postUpdateRequest) {
        final Post findPost = findPost(boardType, postNo);
        findPost.update(postUpdateRequest);
    }

    // 게시글 삭제
    public void deletePost(final BoardType boardType, final Long postNo) {
        final Post findPost = findPost(boardType, postNo);

        // 삭제 상태로 전환
        findPost.setStatus(StatusType.DELETED);

        // 서브 자식이 있다면, 제목 앞에 [원글이 삭제된 답글]을 붙이게 한다.
        for (Post childPost : findPost.getChildrenList()) {
            childPost.setPostContent("[원글이 삭제된 답글] " + childPost.getPostContent());
        }
    }
}

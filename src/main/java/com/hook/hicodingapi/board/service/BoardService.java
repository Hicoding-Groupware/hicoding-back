package com.hook.hicodingapi.board.service;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.board.domain.repository.BoardCriteriaRepository;
import com.hook.hicodingapi.board.domain.repository.BoardRepository;
import com.hook.hicodingapi.board.domain.type.BoardType;
import com.hook.hicodingapi.board.dto.request.PostCreationRequest;
import com.hook.hicodingapi.board.dto.request.PostUpdateRequest;
import com.hook.hicodingapi.board.dto.response.PostCreationResponse;
import com.hook.hicodingapi.board.dto.response.PostReadResponse;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardCriteriaRepository boardCriteriaRepository;

    public List<Post> findAllPosts(final BoardType boardType) {

        final List<Post> postList = boardRepository.findAll();
        // 없거나, 비어있는 경우
        if (postList == null || postList.isEmpty()) {
            throw new NotFoundException(NOT_FOUND_POST_CODE);
        }

        return postList;
    }

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
                parentNo == null ? null : boardRepository.findById(postCreationReq.getParentNo())
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
        //boardRepository.deleteById(postNo);

        final Post findPost = findPost(boardType, postNo);
        findPost.setStatus(StatusType.DELETED);
    }
}

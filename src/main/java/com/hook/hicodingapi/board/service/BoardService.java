package com.hook.hicodingapi.board.service;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.board.domain.repository.BoardCriteriaRepository;
import com.hook.hicodingapi.board.domain.repository.BoardRepository;
import com.hook.hicodingapi.board.domain.type.BoardCriteriaConditionType;
import com.hook.hicodingapi.board.domain.type.BoardType;
import com.hook.hicodingapi.board.dto.request.PostCreationRequest;
import com.hook.hicodingapi.board.dto.request.PostEditRequest;
import com.hook.hicodingapi.board.dto.response.PostReadResponse;
import com.hook.hicodingapi.comment.domain.Comment;
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
public class BoardService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardCriteriaRepository boardCriteriaRepository;

    // 게시판의 게시글 전체 가져오기
    @Transactional(readOnly = true)
    public List<Post> findBoardAllPosts(BoardType boardType) {

        final List<Post> findPostList = boardCriteriaRepository.getPostListByCondition(
                BoardCriteriaConditionType.ALL_POST,
                boardType,
                null
        ).orElseThrow(() -> new CustomException(NOT_FOUND_POSTS_CODE));

        return findPostList;
    }

    // 게시글 조회
    public Post findPost(final BoardType boardType, final Long postNo) {
        final Post findPost = boardRepository.findByPostNoAndBoardTypeAndStatus(postNo, boardType, StatusType.USABLE)
                .orElseThrow(() -> new CustomException(NOT_FOUND_POST_CODE));

        // 조회수 갱신
        findPost.setViews(findPost.getViews() + 1);

        return findPost;
    }

    // 게시글 등록
    public Post createPost(final BoardType boardType, final PostCreationRequest postCreationReq) {

        final Post newPost = Post.of(boardType, postCreationReq)
                .orElseThrow(() -> new CustomException(FAIL_CREATION_POST_CODE));

        newPost.setWriter(
                memberRepository.findByMemberNo(postCreationReq.getWriterNo())
                        .orElseThrow(() -> new CustomException(NOT_FOUND_WRITER_CODE))
        );

        // parent가 있다는 것은 현재 요청된 게시글이 답글 게시글이라는 것
        final Long parentNo = postCreationReq.getParentNo();
        newPost.setParent(
                parentNo == null ?
                        null : boardRepository.findById(parentNo)
                        .orElse(null)
        );

        boardRepository.save(newPost);
        return newPost;
    }

    // 게시글 수정
    public Post updatePost(final BoardType boardType, final Long postNo, final PostEditRequest postEditRequest) {
        final Post editPost = boardRepository.findByPostNoAndBoardTypeAndStatus(postNo, boardType, StatusType.USABLE)
                .orElseThrow(() -> new CustomException(NOT_FOUND_POST_CODE));

        editPost.update(postEditRequest);
        return editPost;
    }

    // 게시글 삭제
    public void deletePost(final BoardType boardType, final Long postNo) {
        final Post deletionPost = boardRepository.findByPostNoAndBoardTypeAndStatus(postNo, boardType, StatusType.USABLE)
                .orElseThrow(() -> new CustomException(NOT_FOUND_POST_CODE));

        boardRepository.delete(deletionPost);

//        // 삭제 상태로 전환
//        deletionPost.setStatus(StatusType.DELETED);
//
//        // 서브 자식이 있다면, 제목 앞에 [원글이 삭제된 답글]을 붙이게 한다.
//        for (Post childPost : deletionPost.getChildrenList()) {
//            childPost.setPostContent("[원글이 삭제된 답글] " + childPost.getPostContent());
//        }
    }

    // 답글을 가진 게시글들을 구조 분해시켜 응답 DTO에 담는다.
    public List<PostReadResponse> convertHierarchicalPostList(final List<Post> postList) {

        // 답글이 없는 게시글(부모)는 List에 담고, 답글 게시글은 부모를 불러(체인) 연결하는 계층 알고리즘
        List<PostReadResponse> postReadResponseList = new ArrayList<>();
        Map<Long, PostReadResponse> postReadResponseHashMap = new HashMap<>();

        postList.forEach(currPost -> {
            PostReadResponse currPostReadResponse = PostReadResponse.from(currPost);

            convertObjChildrenToReadResponseObjChildren(currPostReadResponse, null, currPost.getCommentList());

            postReadResponseHashMap.put(currPostReadResponse.getNo(), currPostReadResponse);

            if (currPost.getParent() != null) {
                PostReadResponse parent = postReadResponseHashMap.get(currPost.getParent().getPostNo());

                // 게시글 답글들이 있다면
                if (parent.getChildrenList() == null)
                    parent.setChildrenList(new ArrayList<>());

                parent.getChildrenList().add(currPostReadResponse);
            } else {
                postReadResponseList.add(currPostReadResponse);
            }
        });

        return postReadResponseList;
    }

    // 특정 자식들을 응답의 자식들로 변환
    public void convertObjChildrenToReadResponseObjChildren(final PostReadResponse postReadResponse,
                                                            final List<Post> postChildrenList,
                                                            final List<Comment> commentList) {

        // 둘 다 NULL이라면
        boolean isNull = postChildrenList == null && commentList == null;
        if (isNull) {
            if (postChildrenList == null)
                throw new CustomException(NOT_FOUND_POSTS_CODE);
            else
                throw new CustomException(NOT_FOUND_COMMENTS_CODE);
        }

        if (postChildrenList != null) {
            if (postReadResponse.getChildrenList() == null) {
                postReadResponse.setChildrenList(new ArrayList<>());

                postChildrenList.forEach(childPost -> {
                    final PostReadResponse convertedPost = PostReadResponse.from(childPost);
                    postReadResponse.getChildrenList().add(convertedPost);
                });
            }

        }

        if (commentList != null) {
            if (postReadResponse.getCommentList() == null) {
                postReadResponse.setCommentList(new ArrayList<>());

                commentList.forEach(childComment -> {
                    final CommentReadResponse convertedComment = CommentReadResponse.from(childComment);
                    postReadResponse.getCommentList().add(convertedComment);
                });
            }
        }
    }
}
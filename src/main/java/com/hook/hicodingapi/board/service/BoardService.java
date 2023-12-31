package com.hook.hicodingapi.board.service;

import com.hook.hicodingapi.board.domain.BoardRecord;
import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.board.domain.repository.BoardCriteriaRepository;
import com.hook.hicodingapi.board.domain.repository.BoardRepository;
import com.hook.hicodingapi.board.domain.type.BoardCriteriaConditionType;
import com.hook.hicodingapi.board.domain.type.BoardRecordType;
import com.hook.hicodingapi.board.domain.type.BoardRole;
import com.hook.hicodingapi.board.domain.type.BoardType;
import com.hook.hicodingapi.board.dto.request.PostCreationRequest;
import com.hook.hicodingapi.board.dto.request.PostEditRequest;
import com.hook.hicodingapi.board.dto.response.PostReadResponse;
import com.hook.hicodingapi.comment.domain.Comment;
import com.hook.hicodingapi.common.domain.type.StatusType;
import com.hook.hicodingapi.common.exception.CustomException;
import com.hook.hicodingapi.member.domain.Member;
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
    private final BoardRecordService boardRecordService;

    // 게시글의 대댓글을 가진 댓글들을 구조 분해시켜 응답 DTO에 담는다.
    // 대댓글이 없는 댓글(부모)은 List에 담고, 대댓글은 부모를 불러(체인) 연결하는 계층 알고리즘
    public List<PostReadResponse> convertHierarchicalPostList(final List<Post> postList) {

        List<PostReadResponse> postReadResponseList = new ArrayList<>();
        Map<Long, PostReadResponse> postReadResponseHashMap = new HashMap<>();

        postList.forEach(currPost -> {
            PostReadResponse currPostReadResponse = PostReadResponse.from(currPost);

            convertObjChildrenToReadResponseObjChildren(currPostReadResponse, null, currPost.getCommentList());

            postReadResponseHashMap.put(currPostReadResponse.getNo(), currPostReadResponse);

            final Post currPostParent = currPost.getParent();
            if (currPostParent != null) {
                PostReadResponse parent = postReadResponseHashMap.get(currPost.getParent().getPostNo());

                // 게시글 답글들이 있다면
                if (parent.getChildrenList() == null)
                    parent.setChildrenList(new ArrayList<>());

                // 자식 깊이 설정
                currPostReadResponse.setDepthLevel(parent.getDepthLevel() + 1);
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

//        if (commentList != null) {
//            if (postReadResponse.getCommentList() == null) {
//                postReadResponse.setCommentList(new ArrayList<>());
//
//                commentList.forEach(childComment -> {
//                    final CommentReadResponse convertedComment = CommentReadResponse.from(childComment);
//                    postReadResponse.getCommentList().add(convertedComment);
//                });
//            }
//        }
    }

    // 게시판 기록 갱신 및 상태 값 처리 로직 (조회수, 좋아요....)
    private void updateBoardRecordTypeOnPost(final BoardRecordType boardRecordType,
                                             final Long postNo,
                                             final Post findPost,
                                             final Member findMember) {

        boolean isPossibleRecord = boardRecordService.isPossibleRecord(boardRecordType, findMember, postNo);
        if (isPossibleRecord) {
            switch (boardRecordType) {
                case VIEWS:
                    findPost.setViews(findPost.getViews() + 1);
                    break;
                case LIKES:
                    findPost.setLikesCount(findPost.getLikesCount() + 1);
                    break;
            }
        }
    }

    // 게시글 가져오기
    private Post findPost(final BoardType boardType, final BoardRole role, final Long postNo) {
        final Post findPost = boardRepository.findByPostNoAndBoardTypeAndRoleAndStatus(postNo, boardType, role, StatusType.USABLE)
                .orElseThrow(() -> new CustomException(NOT_FOUND_POST_CODE));

        return findPost;
    }

    // 게시판의 게시글 전체 가져오기
    @Transactional(readOnly = true)
    public List<Post> findBoardAllPosts(final BoardType boardType, final BoardRole role) {

        final List<Post> findPostList = boardCriteriaRepository.getPostListByCondition(
                BoardCriteriaConditionType.ALL_POST,
                boardType,
                role,
                null
        ).orElseThrow(() -> new CustomException(NOT_FOUND_POSTS_CODE));

        return findPostList;
    }

    // 게시글 조회
    public Post getPost(final BoardType boardType, final BoardRole role, final BoardRecordType boardRecordType, final Long memberNo, final Long postNo) {
        Post findPost = null;
        if(BoardRole.ALL != role) {
            findPost = findPost(boardType, role, postNo);
        }
        else {
            findPost = boardRepository.findByPostNoAndBoardTypeAndStatus(postNo, boardType, StatusType.USABLE)
                    .orElseThrow(() -> new CustomException(NOT_FOUND_POST_CODE));
        }

        final Member findMember = memberRepository.findByMemberNo(memberNo)
                .orElseThrow(() -> new CustomException(NOT_FOUND_READ_MEMBER_CODE));

        updateBoardRecordTypeOnPost(boardRecordType, postNo, findPost, findMember);

        return findPost;
    }

    // 게시글 사용자들 조회
    public List<Member> readPostMembersInfo(final Long postNo, final BoardRecordType boardRecordType) {
        final List<BoardRecord> boardRecordList =
                boardRecordService.findBoardRecordList(postNo, boardRecordType);

        List<Member> postMemberList = new ArrayList<>(boardRecordList.size());
        boardRecordList.forEach(boardRecord -> {
            postMemberList.add(boardRecord.getRecorder());
        });

        return postMemberList;
    }

    // 게시글 등록
    public Post createPost(final BoardType boardType, final BoardRole role, final PostCreationRequest postCreationReq) {
        final Post newPost = Post.of(boardType, role, postCreationReq)
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
    public Post updatePost(final BoardType boardType, final BoardRole role, final Long postNo, final PostEditRequest postEditRequest) {
        final Post editPost = findPost(boardType, role, postNo);
        editPost.update(postEditRequest);
        return editPost;
    }

    // 게시글 삭제
    public void deletePost(final BoardType boardType, final BoardRole role, final Long postNo) {
        final Post deletionPost = findPost(boardType, role, postNo);

        // 삭제 시 oneToMany children db 삭제되는지 확인 용도
        //boardRepository.delete(deletionPost);

        // 삭제 상태로 전환
        deletionPost.setStatus(StatusType.DELETED);

        // 서브 자식이 있다면, 제목 앞에 [원글이 삭제된 답글]을 붙이게 한다.
        for (Post childPost : deletionPost.getChildrenList()) {
            childPost.setPostTitle("[원글이 삭제된 답글] " + childPost.getPostContent());
        }
    }
}
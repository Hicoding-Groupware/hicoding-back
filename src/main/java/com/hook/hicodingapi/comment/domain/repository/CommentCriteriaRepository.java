package com.hook.hicodingapi.comment.domain.repository;

import com.hook.hicodingapi.comment.domain.Comment;
import com.hook.hicodingapi.comment.domain.type.CommentCriteriaConditionType;
import com.hook.hicodingapi.common.domain.repository.BaseCriteriaRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hook.hicodingapi.common.domain.type.StatusType.USABLE;

@Repository
@RequiredArgsConstructor
public class CommentCriteriaRepository {

    private final BaseCriteriaRepository<Comment> baseCriteriaRepository;

    private CriteriaQuery<Comment> findAllCommentsOfPost(final CriteriaBuilder criteriaBuilder, final Long postNo) {

        // CriteriaQuery를 생성하는 부분, JPA에서 사용되는 쿼리를 동적으로 생성할 수 있게 해주는 인터페이스
        CriteriaQuery<Comment> criteriaQuery = criteriaBuilder.createQuery(Comment.class);

        // CriteriaQuery Entity Root
        Root<Comment> root = criteriaQuery.from(Comment.class);

        // 동적 쿼리 관리
        List<Predicate> predicates = new ArrayList<>();

        // fetch join -> 연관된 엔티티를 추가적 쿼리없이 한 번의 쿼리로 모두 가져올 수 있다.
        Join<Comment, Comment> selfJoin = root.join("parent", JoinType.LEFT);
        //Join<Comment, Post> cmtPostJoin = root.join("postCode", JoinType.LEFT);

        // 조건에 따른 필터링 로직은 predicates에 담는다.
        predicates.add(criteriaBuilder.equal(root.get("postCode").get("postNo"), postNo));
//        predicates.add(criteriaBuilder.equal(root.get("status"), USABLE));

        // 정렬 조건은 criteriaQuery에 직접적으로 설정한다.
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("createdAt")));

        // CriteriaQuery에 동적으로 생성된 조건을 추가하는 부분
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return criteriaQuery;
    }

    // 특정 조건에 따른 댓글 조회
    public Optional<List<Comment>> getPostListByCondition(final CommentCriteriaConditionType conditionType, final Long postNo) {

        CriteriaBuilder criteriaBuilder = baseCriteriaRepository.getEntityManager().getCriteriaBuilder();

        // CriteriaQuery를 생성하는 부분, JPA에서 사용되는 쿼리를 동적으로 생성할 수 있게 해주는 인터페이스
        CriteriaQuery<Comment> criteriaQuery = null;

        switch (conditionType) {
            case ALL_COMMENT:
                criteriaQuery = findAllCommentsOfPost(criteriaBuilder, postNo);
                break;
            case PARENT_CHILDREN_COMMENT:
                //criteriaQuery = findParentPostWithChildren(criteriaBuilder, postNo);
                break;
            default:
                break;
        }

        return baseCriteriaRepository.generateCriteriaQuery(criteriaQuery);
    }
}

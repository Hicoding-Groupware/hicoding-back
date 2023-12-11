package com.hook.hicodingapi.board.domain.repository;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.board.domain.type.BoardCriteriaConditionType;
import com.hook.hicodingapi.board.domain.type.BoardType;
import com.hook.hicodingapi.common.domain.repository.BaseCriteriaRepository;
import lombok.*;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hook.hicodingapi.common.domain.type.StatusType.USABLE;

@Repository
@RequiredArgsConstructor
public class BoardCriteriaRepository {

    private final BaseCriteriaRepository<Post> baseCriteriaRepository;

    private CriteriaQuery<Post> findAllPosts(final CriteriaBuilder criteriaBuilder, final BoardType boardType) {

        // CriteriaQuery를 생성하는 부분, JPA에서 사용되는 쿼리를 동적으로 생성할 수 있게 해주는 인터페이스
        CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);

        // CriteriaQuery Entity Root
        Root<Post> root = criteriaQuery.from(Post.class);

        // 동적 쿼리 관리
        List<Predicate> predicates = new ArrayList<>();

        // fetch join -> 연관된 엔티티를 추가적 쿼리 없이 한 번의 쿼리로 모두 가져올 수 있다.
        Join<Post, Post> selfJoin = root.join("parent", JoinType.LEFT);

        // 조건에 따른 필터링 로직은 predicates에 담는다.
        predicates.add(criteriaBuilder.equal(root.get("status"), USABLE));

        // 전체 게시판일 경우 특정 타입 모두를 가져오게 된다.
        if (boardType != BoardType.ALL_BOARD)
            predicates.add(criteriaBuilder.equal(root.get("boardType"), boardType));

        // 정렬 조건은 criteriaQuery에 직접적으로 설정한다.
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("createdAt")));

        // CriteriaQuery에 동적으로 생성된 조건을 추가하는 부분
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return criteriaQuery;
    }

    // CriteriaQuery 및 Root 생성을 담당하는 메소드
    private CriteriaQuery<Post> findParentPostWithChildren(final CriteriaBuilder criteriaBuilder,
                                                           final BoardType boardType,
                                                           final Long postNo) {

        // CriteriaQuery를 생성하는 부분, JPA에서 사용되는 쿼리를 동적으로 생성할 수 있게 해주는 인터페이스
        CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);

        // CriteriaQuery Entity Root
        Root<Post> root = criteriaQuery.from(Post.class);

        // 동적 쿼리 관리
        List<Predicate> predicates = new ArrayList<>();

        // fetch join -> 연관된 엔티티를 추가적 쿼리 없이 한 번의 쿼리로 모두 가져올 수 있다.
        Join<Post, Post> selfJoin = root.join("parent", JoinType.LEFT);

        if (postNo != null) {
            Predicate postNoPredicate = criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("postNo"), postNo),
                    criteriaBuilder.equal(root.get("parent").get("postNo"), postNo)
            );

            predicates.add(postNoPredicate);
        }

        // 정렬 조건은 criteriaQuery에 직접적으로 설정한다.
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("createdAt")));

        // CriteriaQuery에 동적으로 생성된 조건을 추가하는 부분
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        return criteriaQuery;
    }

    // 특정 조건에 따른 게시물 조회
    public Optional<List<Post>> getPostListByCondition(final BoardCriteriaConditionType conditionType,
                                                       final BoardType boardType,
                                                       final Long postNo) {

        CriteriaBuilder criteriaBuilder = baseCriteriaRepository.getEntityManager().getCriteriaBuilder();

        // CriteriaQuery를 생성하는 부분, JPA에서 사용되는 쿼리를 동적으로 생성할 수 있게 해주는 인터페이스
        CriteriaQuery<Post> criteriaQuery = null;

        switch (conditionType) {
            case ALL_POST:
                criteriaQuery = findAllPosts(criteriaBuilder, boardType);
                break;
            case PARENT_CHILDREN_POST:
                criteriaQuery = findParentPostWithChildren(criteriaBuilder, boardType, postNo);
                break;
            default:
                break;
        }

        return baseCriteriaRepository.generateCriteriaQuery(criteriaQuery);
    }
}
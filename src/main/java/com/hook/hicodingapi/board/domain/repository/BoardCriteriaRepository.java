package com.hook.hicodingapi.board.domain.repository;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.board.domain.type.BoardCriteriaConditionType;
import com.hook.hicodingapi.board.domain.type.BoardType;
import com.hook.hicodingapi.board.dto.response.PostQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.hook.hicodingapi.common.domain.type.StatusType.DELETED;
import static com.hook.hicodingapi.common.domain.type.StatusType.USABLE;

@Repository
@RequiredArgsConstructor
public class BoardCriteriaRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private CriteriaQuery<Post> findAllPost(final CriteriaBuilder criteriaBuilder, final BoardType boardType) {

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
    public Optional<List<Post>> getPostListByCondition(final BoardCriteriaConditionType conditionType, final BoardType boardType) {
        /*
        entityManager은 JPA에서 엔터티 매니저를 나타내는 객체,
        getCriteriaBuilder() 메소드는 CriteriaBuilder 객체를 생성,
        CriteriaBuilder는 JPA에서 동적인 쿼리를 생성하는 데 사용되는 빌더 역할
        이것을 통해 CriteriaQuery, Predicate, Root, Join 등의 객체를 생성 및 JPA 쿼리를 조립
        * */
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        // CriteriaQuery를 생성하는 부분, JPA에서 사용되는 쿼리를 동적으로 생성할 수 있게 해주는 인터페이스
        CriteriaQuery<Post> criteriaQuery = null;

        switch (conditionType) {
            case FIND_ALL_POST:
                criteriaQuery = findAllPost(criteriaBuilder, boardType);
                break;
            case FIND_PARENT_POST_WITH_CHILDREN:
                break;
            default:
                break;
        }

        // 1. 여러 단계를 거쳐 작성한 criteriaQuery를 TypedQuery<Entity>로 변환 후
        // 2. getResultList() 메소드를 호출하여 최종적으로 쿼리를 실행하고 결과를 가져오는 부분
        // 3. TypedQuery는 반환될 엔터티 클래스를 지정한 쿼리
        TypedQuery<Post> query = entityManager.createQuery(criteriaQuery);

        // 생성된 TypedQuery를 실행하고 결과를 가져오는 부분, getResultList()는 쿼리 실핼 및 결과를 List<Entity>로 반환
        // 만약 값이 없다면 빈 리스트 반환
        return Optional.of(query.getResultList());
    }
}
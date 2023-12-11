package com.hook.hicodingapi.common.domain.repository;

import lombok.*;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Getter
public class BaseCriteriaRepository<T> {

    /*
        entityManager은 JPA에서 엔터티 매니저를 나타내는 객체,
        getCriteriaBuilder() 메소드는 CriteriaBuilder 객체를 생성,
        CriteriaBuilder는 JPA에서 동적인 쿼리를 생성하는 데 사용되는 빌더 역할
        이것을 통해 CriteriaQuery, Predicate, Root, Join 등의 객체를 생성 및 JPA 쿼리를 조립
     */
    @PersistenceContext
    private final EntityManager entityManager;

    public Optional<List<T>> generateCriteriaQuery(CriteriaQuery<T> criteriaQuery) {
        // 1. 여러 단계를 거쳐 작성한 criteriaQuery를 TypedQuery<Entity>로 변환 후
        // 2. getResultList() 메소드를 호출하여 최종적으로 쿼리를 실행하고 결과를 가져오는 부분
        // 3. TypedQuery는 반환될 엔터티 클래스를 지정한 쿼리
        TypedQuery<T> query = entityManager.createQuery(criteriaQuery);

        // 생성된 TypedQuery를 실행하고 결과를 가져오는 부분, getResultList()는 쿼리 실핼 및 결과를 List<Entity>로 반환
        // 만약 값이 없다면 빈 리스트 반환
        return Optional.of(query.getResultList());
    }
}

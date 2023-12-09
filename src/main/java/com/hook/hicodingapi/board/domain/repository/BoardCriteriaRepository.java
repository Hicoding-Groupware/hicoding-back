package com.hook.hicodingapi.board.domain.repository;

import com.hook.hicodingapi.board.domain.Post;
import com.hook.hicodingapi.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.persistence.EntityManager;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardCriteriaRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public List<Post> findPostByTicketId(Long ticketId) {
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Post> criteriaQuery = criteriaBuilder.createQuery(Post.class);
//        Root<Post> root = criteriaQuery.from(Post.class);
//
//        // Left join with parent
//        Join<Post, Post> parentJoin = root.join("parent", javax.persistence.criteria.JoinType.LEFT);
//
//        // Define conditions
//        Predicate ticketIdPredicate = criteriaBuilder.equal(root.get("postNo"), ticketId);
//
//        // Combine conditions
//        Predicate combinedPredicate = criteriaBuilder.and(ticketIdPredicate);
//
//        // Apply conditions to the query
//        criteriaQuery.select(root)
//                .where(combinedPredicate)
//                .orderBy(
//                        parentJoin.get("id").asc().nullsFirst(),
//                        root.get("createdAt").asc()
//                );
//
//        // Execute the query and return the result
//        return entityManager.createQuery(criteriaQuery).getResultList();
        return null;
    }
}
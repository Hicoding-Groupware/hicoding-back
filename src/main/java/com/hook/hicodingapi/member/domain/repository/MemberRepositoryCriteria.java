package com.hook.hicodingapi.member.domain.repository;

import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.dto.request.MemberInquiryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MemberRepositoryCriteria {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Member> searchMembers(final MemberInquiryRequest searchDTO) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> query = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = query.from(Member.class);

        List<Predicate> predicates = new ArrayList<>();

        // 상세 조회
        if (searchDTO.getId() != null && !searchDTO.getId().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("memberId"), "%" + searchDTO.getId() + "%"));
        }

        if (searchDTO.getName() != null && !searchDTO.getName().isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("memberName"), "%" + searchDTO.getName() + "%"));
        }

        if (searchDTO.getGender() != null) {
            predicates.add(criteriaBuilder.equal(root.get("memberGender"), searchDTO.getGender()));
        }

        if (searchDTO.getRole() != null) {
            predicates.add(criteriaBuilder.equal(root.get("memberRole"), searchDTO.getRole()));
        }

        if (searchDTO.getStatus() != null) {
            predicates.add(criteriaBuilder.equal(root.get("memberStatus"), searchDTO.getStatus()));
        }

        // 상세 조회 -> 날짜 간격, 범위
        if (searchDTO.getJoinedAt() != null) {
            // start -> end 두 날짜가 있으므로 범위 내 조회한다.
            if (searchDTO.getEndedAt() != null) {
                predicates.add(criteriaBuilder.between(root.get("joinedAt"), searchDTO.getJoinedAt(), searchDTO.getEndedAt()));
            } else { // 그게 아니라면 start 날짜 이후의 데이터를 조회한다.
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("joinedAt"), searchDTO.getJoinedAt()));
            }
        }

        // 정렬
        if (searchDTO.getAppliedOrderDataName() != null && searchDTO.getOrderStatus() != null) {
            SortOrder curOrderStatus = searchDTO.getOrderStatus();
            if (curOrderStatus != SortOrder.UNSORTED) {

                Path<String> orderByPath = null;

                boolean isPass = true;
                // 상세 조회에서 MemberRole, MemberStatus가 정렬 기준에도 들어가는 경우라면 정렬 기준에 넣을 수 없다.
                if (searchDTO.getRole() != null &&
                        searchDTO.getAppliedOrderDataName().equals("memberRole")) {
                    isPass = false;
                }
                else if (searchDTO.getStatus() != null &&
                        searchDTO.getAppliedOrderDataName().equals("memberStatus")) {
                    isPass = false;
                }

                if(isPass) {
                    switch (searchDTO.getAppliedOrderDataName()) {
                        case "memberNo":
                        case "memberId":
                        case "memberName":
                        case "memberGender":
                            // age는 Entity 필드 값이 존재하지 않으므로, 보류
                        case "memberAge":
                        case "memberStatus":
                        case "memberRole":
                            orderByPath = root.get(searchDTO.getAppliedOrderDataName());
                            break;
                        default:
                            // exception 처리할 것
                            break;
                    }

                    if (orderByPath != null) {
                        Order selectedOrder;
                        if (curOrderStatus == SortOrder.ASCENDING) {
                            selectedOrder = criteriaBuilder.asc(orderByPath);
                        } else {
                            selectedOrder = criteriaBuilder.desc(orderByPath);
                        }

                        query.orderBy(selectedOrder);
                    }
                }
            }
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }
}

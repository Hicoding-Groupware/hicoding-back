package com.hook.hicodingapi.member.domain.repository;

import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    // 정렬된 기준, 가장 첫 직원 조회
    Optional<Member> findTop1ByMemberRole(MemberRole memberRole, Sort sort);

    // 정렬된 기준, 가장 첫 직원 조회 + 상태 값 조건
    Optional<Member> findTop1ByMemberRoleAndMemberStatusNot(MemberRole memberRole, MemberStatus memberStatus, Sort sort);

    // 직원 전체 삭제
    void deleteAll();

    // 현재 저장된 직원 수 조회
    long count();

    Optional<Member> findByMemberId(String memberId);

    Optional<Member> findByRefreshToken(String refreshToken);

    Optional<Member> findByMemberNo(Long memberNo);

    List<Member> findByMemberNameContaining(String memberName);
}

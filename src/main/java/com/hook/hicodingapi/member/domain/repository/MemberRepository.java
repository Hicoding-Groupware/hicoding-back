package com.hook.hicodingapi.member.domain.repository;

import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findTop1ByMemberRoleAndMemberStatusNot(MemberRole memberRole, MemberStatus memberStatus, Sort sort);
}

package com.hook.hicodingapi.member.domain.repository;

import com.hook.hicodingapi.member.domain.Member;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

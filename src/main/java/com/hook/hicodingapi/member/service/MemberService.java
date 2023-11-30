/*
package com.hook.hicodingapi.member.service;

import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import com.hook.hicodingapi.member.dto.request.MemberInformationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 1. 회원가입


    public void information(final MemberInformationRequest memberRequest){

        final Member newMember = Member.of(
                passwordEncoder.encode(memberRequest.getMemberPwd()),
                memberRequest.getPostNo(),
                memberRequest.getAddress(),
                memberRequest.getDetailAddress(),
                memberRequest.getMemberEmail(),
                memberRequest.getMemberPhone(),
                memberRequest.getMemberBirth(),
                memberRequest.getMemberGender()

        );

        memberRepository.save(newMember);  //save를 통해 여기다가 엔티티를 저장한다.
    }
}
*/

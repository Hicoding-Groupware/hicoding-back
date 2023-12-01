package com.hook.hicodingapi.login.service;

import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {


    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {

        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UsernameNotFoundException("해당 아이디가 존재하지 않습니다."));

        return User.builder()
                .username(member.getMemberId())
                .password(member.getMemberPwd())
                .roles(member.getMemberRole().name())  //admin, teacher, staff
                .build();
    }
}

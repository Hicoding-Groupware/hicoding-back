package com.hook.hicodingapi.msg.dto.response;

import com.hook.hicodingapi.member.domain.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberListResponse {

    private final Long memberNo;
    private final String memberId;
    private final String memberName;


    public static MemberListResponse from(Member member) {
        return new MemberListResponse(
                member.getMemberNo(),
                member.getMemberId(),
                member.getMemberName()
        );
    }
}

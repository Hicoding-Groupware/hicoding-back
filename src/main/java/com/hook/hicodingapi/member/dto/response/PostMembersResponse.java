package com.hook.hicodingapi.member.dto.response;

import com.hook.hicodingapi.informationProvider.domain.type.GenderType;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@AllArgsConstructor
@Getter
public class PostMembersResponse {
    private Long no;
    private String id;
    private String name;

    @Enumerated(value = STRING)
    private GenderType gender;

    private String profile;
    private String email;

    @Enumerated(value = STRING)
    private MemberRole role;

    public static PostMembersResponse from(final Member postMember) {
        return new PostMembersResponse(
                postMember.getMemberNo(),
                postMember.getMemberId(),
                postMember.getMemberName(),
                postMember.getMemberGender(),
                postMember.getMemberProfile(),
                postMember.getMemberEmail(),
                postMember.getMemberRole()
        );
    }
}

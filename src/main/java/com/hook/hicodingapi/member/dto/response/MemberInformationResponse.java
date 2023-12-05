package com.hook.hicodingapi.member.dto.response;

import com.hook.hicodingapi.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class MemberInformationResponse {

    private final String memberName;

    public static MemberInformationResponse from(final Member member){
        return new MemberInformationResponse(
                member.getMemberName()
        );
    }

}

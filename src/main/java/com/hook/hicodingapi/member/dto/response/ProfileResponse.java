package com.hook.hicodingapi.member.dto.response;

import com.hook.hicodingapi.informationProvider.domain.type.GenderType;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ProfileResponse {
    private final String memberId;
    private final String memberName;
    private final String memberPhone;
    private final String memberEmail;
    private final String memberProfile;
    private final MemberRole memberRole;
    private final LocalDateTime joinedAt;


    public static ProfileResponse from(final Member member) {
        return new ProfileResponse(
        member.getMemberId(),
        member.getMemberName(),
        member.getMemberPhone(),
        member.getMemberEmail(),
        member.getMemberProfile(),
        member.getMemberRole(),
        member.getJoinedAt()

        );

    }
}

package com.hook.hicodingapi.member.dto.response;

import com.hook.hicodingapi.informationProvider.domain.type.GenderType;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor(access = PRIVATE)
public class ProfileResponse {
    private final String memberId;
    private final String memberPwd;
    private final String postNo;
    private final String address;
    private final String detailAddress;
    private final String memberName;
    private final String memberPhone;
    private final String memberEmail;
    private final LocalDate memberBirth;
    private final String memberProfile;
    private final GenderType memberGender;
    private final MemberRole memberRole;
    private final LocalDateTime joinedAt;


    public static ProfileResponse from(final Member member, PasswordEncoder passwordEncoder) {
        return new ProfileResponse(
        member.getMemberId(),
        passwordEncoder.encode(member.getMemberPwd()),
        member.getPostNo(),
        member.getAddress(),
        member.getDetailAddress(),
        member.getMemberName(),
        member.getMemberPhone(),
        member.getMemberEmail(),
        member.getMemberBirth(),
        member.getMemberProfile(),
        member.getMemberGender(),
        member.getMemberRole(),
        member.getJoinedAt()

        );

    }
}

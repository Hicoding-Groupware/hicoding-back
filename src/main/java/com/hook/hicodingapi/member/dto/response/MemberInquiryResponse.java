package com.hook.hicodingapi.member.dto.response;

import com.hook.hicodingapi.informationProvider.domain.type.GenderType;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import com.hook.hicodingapi.member.dto.request.MemberCreationRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.hook.hicodingapi.informationProvider.service.InformationProviderService.calculateAge;
import static javax.persistence.EnumType.STRING;

@AllArgsConstructor
@Getter
public class MemberInquiryResponse {
    private int no;
    private String id;
    private String name;
    private String gender;
    private LocalDate birth;
    private int age;
    private String phone;
    private String address;
    private String email;

    @Enumerated(value = STRING)
    private MemberRole role;

    @Enumerated(value = STRING)
    private MemberStatus status;

    private LocalDate joinedAt;
    private LocalDate endedAt;

    public static MemberInquiryResponse from(int idx, Member member) {

        LocalDate birth = member.getMemberBirth();
        final LocalDateTime endedAt = member.getEndedAt();

        return new MemberInquiryResponse(
                idx,
                member.getMemberId(),
                member.getMemberName(),
                member.getMemberGender() != null ?
                    member.getMemberGender().getGender() :
                        null,
                birth,
                // 만 나이
                birth != null ?
                        calculateAge(birth, LocalDate.now()) :
                        -1,
                member.getMemberPhone(),
                member.getPostNo() + ' ' + member.getAddress() + member.getDetailAddress(),
                member.getMemberEmail(),
                member.getMemberRole() != null ?
                        member.getMemberRole() :
                        null,
                member.getMemberStatus() != null ?
                        member.getMemberStatus() :
                        null,
                member.getJoinedAt().toLocalDate(),
                endedAt != null ?
                        endedAt.toLocalDate() :
                        null
        );
    }
}

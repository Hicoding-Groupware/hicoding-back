package com.hook.hicodingapi.member.dto.request;

import com.hook.hicodingapi.informationProvider.domain.type.GenderType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class MemberUpdateRequestWithoutPassword {
    private String memberId;

    private String postNo;

    private String address;

    private String detailAddress;

    private String memberEmail;

    private String memberPhone;

    private LocalDate memberBirth;

    private GenderType memberGender;

}

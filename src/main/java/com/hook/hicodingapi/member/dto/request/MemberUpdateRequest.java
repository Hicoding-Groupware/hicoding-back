package com.hook.hicodingapi.member.dto.request;

import com.hook.hicodingapi.informationProvider.domain.type.GenderType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class MemberUpdateRequest {

    private String memberId;

    private String memberPwd;

    private String postNo;

    private String address;

    private String detailAddress;

    private String memberEmail;

    private String memberPhone;

    private LocalDate memberBirth;

    private GenderType memberGender;

    private String loginStatus;

}

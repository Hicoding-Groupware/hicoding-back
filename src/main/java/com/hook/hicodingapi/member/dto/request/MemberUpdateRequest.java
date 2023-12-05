package com.hook.hicodingapi.member.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class MemberUpdateRequest {

    @NotBlank
    private String memberId;
    @NotBlank
    private String memberPwd;
    @NotBlank
    private String postNo;
    @NotBlank
    private String address;
    @NotBlank
    private String detailAddress;
    @NotBlank
    private String memberEmail;
    @NotBlank
    private String memberPhone;
    @NotBlank
    private LocalDate memberBirth;
    @NotNull
    private String memberGender;
}

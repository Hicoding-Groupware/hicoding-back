package com.hook.hicodingapi.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class MemberInformationRequest {
    @NotBlank
    private final String memberPwd;
    @NotBlank
    private final String postNo;
    @NotBlank
    private final String address;
    @NotBlank
    private final String detailAddress;
    @NotBlank
    private final String memberEmail;
    @NotBlank
    private final String memberPhone;
    @NotBlank
    private final String memberBirth;
    @NotBlank
    private final String memberGender;



}

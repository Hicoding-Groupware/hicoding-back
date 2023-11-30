package com.hook.hicodingapi.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

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
    private final LocalDate memberBirth;
    @NotBlank
    private final String memberGender;



}

package com.hook.hicodingapi.member.dto.request;

import com.hook.hicodingapi.member.domain.type.MemberRole;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;

@RequiredArgsConstructor
@Getter
public class MemberRandomCreationRequest {
    @NotNull
    @NotBlank
    private final String memberPwd;

    @NotNull
    @Min(1)
    private final int mbrCnt;
}

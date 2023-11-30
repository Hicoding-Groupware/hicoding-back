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
public class MemberCreationRequest {
    @NotNull(message = "이름이 반드시 들어가야 합니다.")
    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    private final String memberName;

    @NotNull(message = "부서에 반드시 소속되어야 합니다.")
    @Enumerated(value = STRING)
    private final MemberRole memberRole;

    @NotNull
    @Min(1)
    private final int cnt;
}

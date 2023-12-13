package com.hook.hicodingapi.member.dto.request;

import com.hook.hicodingapi.informationProvider.domain.type.GenderType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class MemberProfileUpdate {
    private String memberId;

}

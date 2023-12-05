package com.hook.hicodingapi.member.dto.request;

import com.hook.hicodingapi.informationProvider.domain.type.GenderType;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class MemberInquiryRequest {
    private String id;
    private String name;
    private GenderType gender;
    private MemberRole role;
    private MemberStatus status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate joinedAt;

    private LocalDate endedAt;

    private String appliedOrderDataName;
    private SortOrder orderStatus;
}

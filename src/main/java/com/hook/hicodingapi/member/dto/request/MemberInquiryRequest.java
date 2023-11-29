package com.hook.hicodingapi.member.dto.request;

import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
public class MemberInquiryRequest {
    @NonNull
    private String memberId;

    @NonNull
    private String memberName;

    @NonNull
    private MemberRole deptName;

    @NonNull
    private MemberStatus memberStatus;

    @NonNull
    private LocalDateTime joinedAt;

    @NonNull
    private LocalDateTime endedAt;
}

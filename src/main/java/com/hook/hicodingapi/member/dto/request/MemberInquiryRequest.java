package com.hook.hicodingapi.member.dto.request;

import com.hook.hicodingapi.informationIdentifier.domain.type.GenderType;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import lombok.*;

import javax.swing.*;
import java.time.LocalDateTime;

@Builder
@Getter
public class MemberInquiryRequest {
    private String memberId;
    private String memberName;
    private MemberRole memberRole;
    private MemberStatus memberStatus;
    private LocalDateTime joinedAt;
    private LocalDateTime endedAt;

    private String appliedOrderDataName;
    private SortOrder orderStatus;
}

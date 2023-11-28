package com.hook.hicodingapi.member.domain;

import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import com.hook.hicodingapi.member.dto.MemberGenerateRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDateTime;

import static com.hook.hicodingapi.member.domain.type.MemberStatus.ACTIVE;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_member")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Member {
    public static final Integer MAX_DEPT_NUM = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @NotNull
    private String memberId;

    @NotNull
    private String memberPwd;

    @NotNull(message = "이름은 반드시 입력되어야 합니다.")
    private String memberName;

    private String memberGender;
    private String memberBirth;
    private String memberPhone;
    private String memberEmail;
    private String memberProfile;
    private String post_no;
    private String address;
    private String detailAddress;

    @NotNull
    @Enumerated(value = STRING)
    private MemberStatus memberStatus = ACTIVE;

    @NotNull(message = "부서에 반드시 소속되어야 합니다.")
    @Enumerated(value = STRING)
    private MemberRole memberRole;

    @Min(1)
    private Integer registrationNo;

    @NotNull
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @NotNull
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime joinedAt;

    private LocalDateTime endedAt;
    private String refreshToken;

    public Member(Integer registrationNo) {
        this.registrationNo = registrationNo;
    }

    private Member(String memberId, String memberPwd, String memberName, MemberRole memberRole, Integer registrationNo) {
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.memberName = memberName;
        this.memberRole = memberRole;
        this.registrationNo = registrationNo;
    }

    public static Member of(String memberId, String memberPwd, MemberGenerateRequest memberGenerateRequest, Integer registrationNo) {
        return new Member(
                memberId,
                memberPwd,
                memberGenerateRequest.getMemberName(),
                memberGenerateRequest.getMemberRole(),
                registrationNo
        );
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

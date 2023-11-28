package com.hook.hicodingapi.member.domain;

import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.hook.hicodingapi.member.domain.type.MemberStatus.ACTIVE;
import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "tbl_member")
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Getter
public class Member {
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

    @NotNull
    @CreatedDate
    @Column(updatable = false)
    private String createdAt;

    @NotNull
    @CreatedDate
    @Column(updatable = false)
    private String joinedAt;

    private String endedAt;
    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

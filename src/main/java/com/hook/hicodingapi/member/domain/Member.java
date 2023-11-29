package com.hook.hicodingapi.member.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import com.hook.hicodingapi.member.dto.request.MemberCreationRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
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
    private LocalDate memberBirth;
    private String memberPhone;
    private String memberEmail;
    private String memberProfile;
    private String postNo;
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

    public Member(String memberId, String memberPwd, String memberName,
                  String memberGender, LocalDate memberBirth, String memberPhone,
                  String memberEmail, String post_no, String address,
                  String detailAddress, MemberStatus memberStatus, MemberRole memberRole,
                  Integer registrationNo) {
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.memberName = memberName;
        this.memberGender = memberGender;
        this.memberBirth = memberBirth;
        this.memberPhone = memberPhone;
        this.memberEmail = memberEmail;
        this.post_no = post_no;
        this.address = address;
        this.detailAddress = detailAddress;
        this.memberStatus = memberStatus;
        this.memberRole = memberRole;
        this.registrationNo = registrationNo;
    }

    public static Member of(String memberId, String memberPwd, MemberCreationRequest memberCreationRequest, Integer registrationNo) {
        return new Member(
                memberId,
                memberPwd,
                memberCreationRequest.getMemberName(),
                memberCreationRequest.getMemberRole(),
                registrationNo
        );
    }


    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

public Member(String memberPwd, String postNo, String address, String detailAddress, String memberEmail, String memberPhone, String memberBirth, String memberGender) {

        this.memberPwd = memberPwd;
        this.postNo = postNo;
        this.address = address;
        this.detailAddress = detailAddress;
        this.memberEmail = memberEmail;
        this.memberPhone = memberPhone;
        this.memberBirth = memberBirth;
        this.memberGender = memberGender;
}

public static Member of(String memberPwd, String postNo, String address, String detailAddress, String memberEmail, String memberPhone, String memberBirth, String memberGender) {

        return new Member(
                memberPwd,
                postNo,
                address,
                detailAddress,
                memberEmail,
                memberPhone,
                memberBirth,
                memberGender
        ); //(이렇게 전달된 값을 entity로 만들어주는 메소드)
}
}

package com.hook.hicodingapi.member.service;
import com.hook.hicodingapi.common.util.FileUploadUtils;
import com.hook.hicodingapi.informationProvider.domain.type.GenderType;
import com.hook.hicodingapi.member.dto.request.*;
import com.hook.hicodingapi.member.dto.response.PreLoginResponse;
import com.hook.hicodingapi.common.exception.NotFoundException;
import com.hook.hicodingapi.member.domain.MemberDataSender;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import com.hook.hicodingapi.member.domain.repository.MemberCriteriaRepository;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import com.hook.hicodingapi.member.dto.request.MemberCreationRequest;
import com.hook.hicodingapi.member.dto.request.MemberInquiryRequest;
import com.hook.hicodingapi.member.dto.response.MemberCreationResponse;
import com.hook.hicodingapi.member.dto.response.MemberInquiryResponse;
import com.hook.hicodingapi.member.dto.response.ProfileResponse;
import com.hook.hicodingapi.student.dto.response.StudentCourse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.stream.IntStream;

import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_COS_CODE;
import static com.hook.hicodingapi.common.exception.type.ExceptionCode.NOT_FOUND_MEMBER_ID;
import static com.hook.hicodingapi.member.domain.Member.MAX_DEPT_NUM;
import static com.hook.hicodingapi.informationProvider.service.InformationProviderService.*;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberCriteriaRepository memberCriteriaRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${image.image-url}")
    private String IMAGE_URL;
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    @PersistenceContext
    private EntityManager entityManager;

    // 임시 비밀번호 생성
    private String generatePwd() {
        // 8 ~ 16자의 숫자 + 영문 조합을 생성한다.
        // 임시 비밀번호 객체를 생성한다.
        StringBuilder tempPwd = new StringBuilder();

        // 8 ~ 16자의 자릿수 랜덤 생성
        final int PWD_LEN = (int) (Math.random() * 9 + 8);

        // 각 자릿수에 숫자 | 대 | 소문자 Char 값을 넣는다.
        for (int i = 0; i < PWD_LEN; i++) {
            char selectedVal = ' ';
            switch ((int) (Math.random() * 3)) {
                // 숫자
                case 0:
                    selectedVal = (char) ((int) (Math.random() * 9 + 1) + '0');
                    break;
                // 소문자    26
                case 1:

                    int lAlphabet = (int) (Math.random() * 25 + 65);
                    selectedVal = (char) (lAlphabet);
                    break;
                // 대문자
                case 2:
                    int bAlphabet = (int) (Math.random() * 25 + 97);
                    selectedVal = (char) (bAlphabet);
                    break;
            }

            tempPwd.append(selectedVal);
        }

        return tempPwd.toString();
    }

    // 사번 생성 알고리즘
    @Transactional(readOnly = true)
    public MemberDataSender generateId(final MemberRole departmentName) {

        // --init--

        // 소속 학원 이니셜
        final String academyInitialsName = "hc";

        // 현재년도
        int currentYear = Year.now().getValue();

        // 등록할 번호
        String registerNo = null;

        // --logic--

        // 현재 년도에서 뒤 2자리 가져오기 (입사년도 두자리)
        String caldStrYear = Integer.toString(currentYear).substring(2, 4);

        MemberDataSender mbrIdAndRegNo = generateRegistrationNo(departmentName);
        registerNo = mbrIdAndRegNo.getMemberId();

        // --ret--

        // 데이터들을 조합하여 id 생성
        final String combinedId = academyInitialsName + caldStrYear + departmentName.getRoleNo() + registerNo;
        // 조합된 id를 다시 넣어 보낸다.
        mbrIdAndRegNo.setMemberId(combinedId);
        return mbrIdAndRegNo;
    }

    // 직원 등록번호 생성 알고리즘
    // 입사년도 두자리 + 부서코드 + 등록번호 -> ex 2301001
    public MemberDataSender generateRegistrationNo(final MemberRole departmentName) {
        // 최대 인원 0의 개수
        final int maxZeroLenCnt = Integer.toString(MAX_DEPT_NUM).length() - 1;

        // 추가할 0의 개수
        int addedZeroLenCnt = maxZeroLenCnt;

        // 소속 부서 마지막 등록 번호를 가져온다.
        Optional<Member> lastMemberOptional = memberRepository.findTop1ByMemberRole(departmentName,
                        Sort.by(Sort.Order.desc("registrationNo")))
                // 가져올 수 없다면 등록번호 0 번 초기화 값을 만들어 가져온다.
                .map(Optional::of)
                .orElseGet(() -> Optional.of(new Member(0)));

        final int lastRegisteredNo = lastMemberOptional.get().getRegistrationNo();

        // 가져온 값에 1을 더하여 등록 번호를 갱신한다.
        int caldRegdNumber = lastRegisteredNo + 1;
        // 현재 소속 부서의 인원이 다 차있는 상태입니다.
        if (MAX_DEPT_NUM < caldRegdNumber) {
            System.out.println("해당 부서의 인원이 다 차있는 상태입니다.");
        }

        // 사용될 등록 번호의 0의 개수
        int caldRegZeoLenCnt = Integer.toString(caldRegdNumber).length() - 1;

        // 0의 개수가 하나라도 있다면 최대 인원 0의 개수에서 빼서 추가할 0의 개수에 계산하여 대입한다.
        if (0 < caldRegZeoLenCnt) {
            addedZeroLenCnt = maxZeroLenCnt - caldRegZeoLenCnt;
        }

        // 추가할 0의 개수를 string으로 변환한다.
        StringBuilder zeroStr = new StringBuilder();
        for (int i = 0; i < addedZeroLenCnt; i++) {
            zeroStr.append('0');
        }

        // 추가할 0의 개수 + 사용될 등록 번호를 조합한다.
        String combinedNumber = zeroStr.toString() + Integer.toString(caldRegdNumber);

        // 사용될 0의 개수 + 부서 등록 번호와 등록 번호를 보낸다.
        return new MemberDataSender(combinedNumber, caldRegdNumber);
    }

    // 직원 생성
    @Transactional
    public void customInsert(final MemberCreationRequest memberCreationRequest, List<MemberCreationResponse> memberCreationResponseList) {

        // 회원 id는 사번이며, 사번은 규칙에 의거하여 만들어진다.
        MemberDataSender mbrIdAndRegNo = generateId(memberCreationRequest.getMemberRole());

        // 임시 비밀번호
        String tempPwd = generatePwd();

        // 응답 아이디와 임시 비밀번호 저장
        memberCreationResponseList.add(new MemberCreationResponse(mbrIdAndRegNo.getMemberId(), tempPwd));

        // 비밀번호는 Encoder에 의하여 인코딩 과정이 들어간다.
        String memberPwd = passwordEncoder.encode(tempPwd);

        // 가입일
        final LocalDateTime mbrJoinedAt = LocalDateTime.now();

        final Member newMember = Member.of(
                mbrIdAndRegNo.getMemberId(),
                memberPwd,
                memberCreationRequest,
                mbrIdAndRegNo.getRegistrationNo(),
                mbrJoinedAt
        );

        memberRepository.save(newMember);

        System.out.println(newMember.toString());
    }

    // 직원 랜덤 생성
    @Transactional
    public Member randomInsert(final String inputtedPassword) {

        // pk

        //ID
        // 회원 id는 사번이며, 사번은 규칙에 의거하여 만들어진다.
        final MemberRole mbrDeptType = generateRandomEnumTypeValue(MemberRole.class);
        final MemberDataSender mbrIdAndRegNo = generateId(mbrDeptType);

        // 비번
        // 암호화
        final String memberPwd = passwordEncoder.encode(inputtedPassword);

        // 이름
        final String memberName = generateKoreanName();

        // 성별
        final GenderType memberGender = generateRandomGender();

        // 생년월일
        final LocalDate memberBirth = generateRandomDateTime(1900).toLocalDate();

        // 만 나이
        final int memberAge = calculateAge(memberBirth, LocalDate.now());

        // 연락처
        final String memberPhone = generateRandomPhoneNumber();

        // 이메일
        final String memberEmail = generateMail(mbrIdAndRegNo.getMemberId());

        // 프로필

        // 주소
        // post
        final String mbrPostNo = generateRandomPostNo();

        // addr
        final String mbrAddr = generateRandomAddress();

        // detailAddr
        final String mbrDetailAddR = generateRandomDetailAddress();

        // 재직 상태
        final MemberStatus mbrStatus = generateRandomEnumTypeValue(MemberStatus.class);

        // 소속 부서
        // 위에서 기입됨

        // 등록일

        // 입사일
        final LocalDateTime mbrJoinedAt = generateRandomDateTime(1999);

        // 퇴사일
        final LocalDateTime mbrEndedAt = generateRandomDateTime(mbrJoinedAt.toLocalDate().getYear() + 1);

        // 토큰

        Member newMember = Member.of(mbrIdAndRegNo.getMemberId(), memberPwd, memberName,
                memberGender, memberBirth, memberPhone,
                memberEmail, mbrPostNo, mbrAddr,
                mbrDetailAddR, mbrStatus, mbrDeptType,
                mbrIdAndRegNo.getRegistrationNo(), mbrJoinedAt, mbrEndedAt
        );

        memberRepository.save(newMember);

        return newMember;
    }

    // 전체 직원 조회
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        final List<Member> members = memberRepository.findAll();
        return members;
    }

    // 직원 상세 조회 -> By Criteria
    @Transactional(readOnly = true)
    public List<MemberInquiryResponse> getDetailMembers(final MemberInquiryRequest memberInquiryRequest) {

        final List<Member> members = memberCriteriaRepository.searchMembers(memberInquiryRequest);

//        List<MemberInquiryResponse> mbrInquiryResponseList = members
//                .stream().map((member) -> MemberInquiryResponse.from(member))
//                .collect(Collectors.toList());

        List<MemberInquiryResponse> mbrInquiryResponseList = IntStream.range(0, members.size())
                .mapToObj(index -> MemberInquiryResponse.from(index, members.get(index)))
                .collect(Collectors.toList());

        return mbrInquiryResponseList;
    }

    @Transactional
    // 직원 수정
    public void update(Long memberCode, MemberRole memberRole, MemberStatus memberStatus) {
        Optional<Member> findMember = memberRepository.findById(memberCode);
        findMember.get().update(memberRole, memberStatus);
    }

    // 직원 삭제
    @Transactional
    public void deleteMember(final Long memberCode) {
        memberRepository.deleteById(memberCode);
    }

    // 직원 전체 삭제
    @Transactional
    public void deleteAllMembers() {
        // 이전에 저장된 엔티티의 수를 조회
        long initialCount = memberRepository.count();

        // 엔티티 삭제
        memberRepository.deleteAll();

        // 삭제 후의 엔티티 수를 다시 조회
        long finalCount = memberRepository.count();

        // 삭제된 엔티티의 수 계산
        long deletedCount = initialCount - finalCount;

        // 삭제된 엔티티가 하나 이상이어야 성공으로 간주
        if (deletedCount > 0) {
            System.out.println("삭제 작업이 성공하였습니다. 삭제된 엔티티 수: " + deletedCount);
        } else {
            System.out.println("삭제 작업이 실패하였습니다.");
        }
    }

    /*----------------------------- 민서 존 ------------------------------------------------------------*/

    public PreLoginResponse preLogin(Map<String, String> loginInfo) {
        Member member = memberRepository.findByMemberId(loginInfo.get("memberId"))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER_ID));


        PreLoginResponse preLoginResponse = null;

        if(member.getLoginStatus() == null) {
            preLoginResponse = PreLoginResponse.of(true, member.getMemberId(), member.getMemberName());
        } else {
            preLoginResponse = PreLoginResponse.of(false, null, null);
        }
        return preLoginResponse;
    }



//    public void memberUpdate(MemberUpdateRequest memberUpdateRequest) {
//
//        Optional<Member> optionalMember = memberRepository.findByMemberId(memberUpdateRequest.getMemberId());
//
//
//        if (optionalMember.isPresent()) {
//            Member member = optionalMember.get();
//             /* 업데이트 할때 비밀번호 암호화해줌 */
//            member.update(
//                    passwordEncoder.encode(memberUpdateRequest.getMemberPwd()),
//                    memberUpdateRequest.getPostNo(),
//                    memberUpdateRequest.getAddress(),
//                    memberUpdateRequest.getDetailAddress(),
//                    memberUpdateRequest.getMemberEmail(),
//                    memberUpdateRequest.getMemberPhone(),
//                    memberUpdateRequest.getMemberBirth(),
//                    memberUpdateRequest.getMemberGender(),
//                    "Y"
//            );
//        } else {
//
//            throw new NotFoundException(NOT_FOUND_MEMBER_ID);
//
//
//        }
//    }



    public void memberUpdate(final MemberUpdateRequest memberUpdateRequest) {

        Optional<Member> optionalMember = memberRepository.findByMemberId(memberUpdateRequest.getMemberId());


        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
             /* 업데이트 할때 비밀번호 암호화해줌 */
            member.update(
                    passwordEncoder.encode(memberUpdateRequest.getMemberPwd()),
                    memberUpdateRequest.getPostNo(),
                    memberUpdateRequest.getAddress(),
                    memberUpdateRequest.getDetailAddress(),
                    memberUpdateRequest.getMemberEmail(),
                    memberUpdateRequest.getMemberPhone(),
                    memberUpdateRequest.getMemberBirth(),
                    memberUpdateRequest.getMemberGender(),
                    "Y"
            );
        } else {

            throw new NotFoundException(NOT_FOUND_MEMBER_ID);


        }
    }
    private String getRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }


  // public void save(MultipartFile memberProfile, MemberProfileUpdate memberProfileUpdate) {
  //     /* 전달 된 파일을 서버의 지정 경로에 저장 */
  //     String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, getRandomName(), memberProfile);

  //     Optional<Member> optionalMember = memberRepository.findByMemberId(memberProfileUpdate.getMemberId());

  //    final Member newMember = Member.of(
  //            IMAGE_URL + replaceFileName
  //    )


  // }
  public void save(MultipartFile memberProfile, MemberProfileUpdate memberProfileUpdate) {
      String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, getRandomName(), memberProfile);


      Optional<Member> optionalMember = memberRepository.findByMemberId(memberProfileUpdate.getMemberId());

      if (optionalMember.isPresent()){
          Member existingMember = optionalMember.get();
          existingMember.updateMemberProfile(IMAGE_URL + replaceFileName);

          memberRepository.save(existingMember);
      }else {
          throw new NotFoundException(NOT_FOUND_MEMBER_ID);
      }

  }

   public void memberProfileUpdate(MemberProfileUpdate memberProfileUpdate, MultipartFile memberProfile) {
       Optional<Member> optionalMember = memberRepository.findByMemberId(memberProfileUpdate.getMemberId());

       if (memberProfile != null){
           String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, getRandomName(), memberProfile);
           FileUploadUtils.deleteFile(IMAGE_DIR, optionalMember.get().getMemberProfile().replace(IMAGE_URL, ""));
           optionalMember.get().updateMemberProfileUrl(IMAGE_URL + replaceFileName);
       }

   }


    public void memberUpdateWithoutPassword(MemberUpdateRequestWithoutPassword memberUpdateRequestWithoutPassword) {

                Optional<Member> optionalMember = memberRepository.findByMemberId(memberUpdateRequestWithoutPassword.getMemberId());


        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
             /* 업데이트 할때 비밀번호 암호화해줌 */
            member.update(
                    memberUpdateRequestWithoutPassword.getPostNo(),
                    memberUpdateRequestWithoutPassword.getAddress(),
                    memberUpdateRequestWithoutPassword.getDetailAddress(),
                    memberUpdateRequestWithoutPassword.getMemberEmail(),
                    memberUpdateRequestWithoutPassword.getMemberPhone(),
                    memberUpdateRequestWithoutPassword.getMemberBirth(),
                    memberUpdateRequestWithoutPassword.getMemberGender()
            );
        } else {

            throw new NotFoundException(NOT_FOUND_MEMBER_ID);


        }
    }

    @Transactional
    public ProfileResponse getProfile(String memberId) {

        final Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_MEMBER_ID));

        return ProfileResponse.from(member, passwordEncoder);
    }

    public void deleteProfile(String memberId) {
        final Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow( () -> new NotFoundException(NOT_FOUND_MEMBER_ID));

        if (member.getMemberProfile() != null){
            FileUploadUtils.deleteFile(IMAGE_DIR, member.getMemberProfile().replace(IMAGE_URL, ""));
            String deleteProfile = null;
            member.updateProfile(deleteProfile);
        }

    }


    //       public void delete(String memberId) {
 //       Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);
//
 //       if (optionalMember.isPresent()){
 //           Member deleteProfile = optionalMember.get();
 //           if (deleteProfile.getMemberProfile() != null){
 //               FileUploadUtils.deleteFile(IMAGE_DIR, deleteProfile.getMemberProfile());
 //           }
//
 //       }else {
 //           throw new NotFoundException(NOT_FOUND_MEMBER_ID);
//
 //       }
 //   }


    //동한
    public List<Member> getMemberList() {
        return memberRepository.findAll();
    }
}

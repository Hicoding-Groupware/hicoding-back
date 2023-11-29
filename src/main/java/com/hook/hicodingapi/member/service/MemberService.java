package com.hook.hicodingapi.member.service;

import com.hook.hicodingapi.member.domain.MemberDataSender;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.domain.type.MemberStatus;
import com.hook.hicodingapi.member.dto.request.MemberCreationRequest;
import com.hook.hicodingapi.member.dto.request.MemberInquiryRequest;
import com.hook.hicodingapi.member.dto.response.MemberCreationResponse;
import com.hook.hicodingapi.personalInformation.service.PersonalInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

import static com.hook.hicodingapi.member.domain.Member.MAX_DEPT_NUM;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
    private MemberDataSender generateId(final MemberRole departmentName) {

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
    @Transactional(readOnly = true)
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

        final Member newMember = Member.of(
                mbrIdAndRegNo.getMemberId(),
                memberPwd,
                memberCreationRequest,
                mbrIdAndRegNo.getRegistrationNo()
        );

        memberRepository.save(newMember);
    }

    // 직원 랜덤 생성
    @Transactional
    public void randomInsert(final String inputtedPassword) {
        // pk

        // ID
        // 회원 id는 사번이며, 사번은 규칙에 의거하여 만들어진다.
        final MemberRole mbrDeptType = PersonalInformationService.generateRandomEnumTypeValue(MemberRole.class);
        final MemberDataSender mbrIdAndRegNo = generateId(mbrDeptType);

        // 비번
        // 암호화
        final String memberPwd = passwordEncoder.encode(inputtedPassword);

        // 이름
        final String memberName = PersonalInformationService.generateKoreanName();

        // 성별
        final String memberGender = PersonalInformationService.generateRandomGender();

        // 생년월일
        final LocalDate memberBirth = PersonalInformationService.generateRandomDateTime();

        // 만 나이
        final int memberAge = PersonalInformationService.calculateAge(memberBirth, LocalDate.now());

        // 연락처
        final String memberPhone = PersonalInformationService.generateRandomPhoneNumber();

        // 이메일
        final String memberEmail = PersonalInformationService.generateMail(mbrIdAndRegNo.getMemberId());

        // 프로필

        // 주소
        // post
        final String mbrPostNo = PersonalInformationService.generateRandomPostNo();

        // addr
        final String mbrAddr = PersonalInformationService.generateRandomAddress();

        // detailAddr
        final String mbrDetailAddR = PersonalInformationService.generateRandomDetailAddress();

        // 재직 상태
        final MemberStatus mbrStatus = PersonalInformationService.generateRandomEnumTypeValue(MemberStatus.class);
        
        // 소속 부서
        // 위에서 기입됨
        
        // 등록일

        // 입사일

        // 퇴사일
        
        // 토큰

        Member newMember = new Member(
                mbrIdAndRegNo.getMemberId(), memberPwd, memberName,
                memberGender, memberBirth, memberPhone,
                memberEmail, mbrPostNo, mbrAddr, mbrDetailAddR,
                mbrStatus, mbrDeptType, mbrIdAndRegNo.getRegistrationNo());

        memberRepository.save(newMember);
    }

    // 전체 직원 조회
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {

        final List<Member> members = memberRepository.findAll();
        return members;
    }

    // 직원 상세 조회
    @Transactional(readOnly = true)
    public List<Member> getDetailMembers(final MemberInquiryRequest memberInquiryRequest) {



        return null;
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
}

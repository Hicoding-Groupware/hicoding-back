package com.hook.hicodingapi.member.service;

import com.hook.hicodingapi.member.domain.MemberDataSender;
import com.hook.hicodingapi.member.domain.Member;
import com.hook.hicodingapi.member.domain.repository.MemberRepository;
import com.hook.hicodingapi.member.domain.type.MemberRole;
import com.hook.hicodingapi.member.dto.MemberGenerateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Optional;

import static com.hook.hicodingapi.member.domain.Member.MAX_DEPT_NUM;
import static com.hook.hicodingapi.member.domain.type.MemberStatus.DELETED;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 직원 생성
    @Transactional
    public void create(final MemberGenerateRequest memberGenerateRequest) {
        for (int i = 0; i < 101; i++) {
            // 회원 id는 사번이며, 사번은 규칙에 의거하여 만들어진다.
            MemberDataSender mbrIdAndRegNo = generateId(memberGenerateRequest.getMemberRole());

            // 비밀번호는 Encoder에 의하여 인코딩 과정이 들어간다.
            String memberPwd = passwordEncoder.encode(generatePwd());

            final Member newMember = Member.of(
                    mbrIdAndRegNo.getMemberId(),
                    memberPwd,
                    memberGenerateRequest,
                    mbrIdAndRegNo.getRegistrationNo()
            );

            memberRepository.save(newMember);
        }
    }

    public String generatePwd() {
        // 8 ~ 16자의 숫자 + 영문 조합을 생성한다.
        // 임시 비밀번호 객체를 생성한다.
        StringBuilder tempPwd = new StringBuilder();

        // 8 ~ 16자의 자릿수 랜덤 생성
        final int PWD_LEN = (int)(Math.random() * 9 + 8);

        // 각 자릿수에 숫자 | 대 | 소문자 Char 값을 넣는다.
        for(int i = 0; i < PWD_LEN; i++) {
            char selectedVal = ' ';
            switch ((int)(Math.random() * 3)) {
                // 숫자
                case 0:
                    selectedVal = (char)((int)(Math.random() * 9 + 1) + '0');
                    break;
                // 소문자    26
                case 1:

                    int lAlphabet = (int)(Math.random() * 25 + 65);
                    selectedVal = (char)(lAlphabet);
                    break;
                // 대문자
                case 2:
                    int bAlphabet = (int)(Math.random() * 25 + 97);
                    selectedVal = (char)(bAlphabet);
                    break;
            }

            tempPwd.append(selectedVal);
        }

        return tempPwd.toString();
    }

    // 사번 생성 알고리즘
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

        MemberDataSender mbrIdAndRegNo = generateRegistrationNo(departmentName, 100);
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
    public MemberDataSender generateRegistrationNo(final MemberRole departmentName, int maxStaff) {
        // 최대 인원 0의 개수
        final int maxZeroLenCnt = Integer.toString(MAX_DEPT_NUM).length() - 1;

        // 추가할 0의 개수
        int addedZeroLenCnt = maxZeroLenCnt;

        // 소속 부서 마지막 등록 번호를 가져온다.
        Optional<Member> lastMemberOptional = memberRepository.findTop1ByMemberRoleAndMemberStatusNot(departmentName,
                        DELETED,
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
        int caldRegZeoLenCnt =  Integer.toString(caldRegdNumber).length() - 1;

        // 0의 개수가 하나라도 있다면 최대 인원 0의 개수에서 빼서 추가할 0의 개수에 계산하여 대입한다.
        if (0 < caldRegZeoLenCnt) {
            addedZeroLenCnt = maxZeroLenCnt - caldRegZeoLenCnt;
        }

        // 추가할 0의 개수를 string으로 변환한다.
        StringBuilder zeroStr = new StringBuilder();
        for(int i = 0; i < addedZeroLenCnt; i++) {
            zeroStr.append('0');
        }

        // 추가할 0의 개수 + 사용될 등록 번호를 조합한다.
        String combinedNumber = zeroStr.toString() + Integer.toString(caldRegdNumber);

        // 사용될 0의 개수 + 부서 등록 번호와 등록 번호를 보낸다.
        return new MemberDataSender(combinedNumber, caldRegdNumber);
    }
}

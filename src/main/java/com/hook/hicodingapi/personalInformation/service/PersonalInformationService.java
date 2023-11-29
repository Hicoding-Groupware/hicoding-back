package com.hook.hicodingapi.personalInformation.service;

import com.hook.hicodingapi.personalInformation.domain.type.GenderType;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.hook.hicodingapi.personalInformation.domain.PersonalInformation.*;

@Service
public class PersonalInformationService {

    private final static Random RANDOM_N = new Random();

    // 이름 랜덤 생성 알고리즘
    public static List<String> generateKoreanNames() {
        List<String> result = new ArrayList<>();

        while (result.size() < MAX_PI_NUM) {
            String fullName = KOREAN_SURNAMES.get(RANDOM_N.nextInt(KOREAN_SURNAMES.size())) +
                    KOREAN_GIVEN_NAMES.get(RANDOM_N.nextInt(KOREAN_GIVEN_NAMES.size()));

            if (!result.contains(fullName)) {
                result.add(fullName);
            }
        }

        return result;
    }

    // 년도 랜덤 생성 알고리즘
    public static LocalDateTime generateRandomDateTime() {
        // 현재년도
        int currentYear = Year.now().getValue();
        // 년도 뒤 두 자리 계산
        String caldStrYear = Integer.toString(currentYear).substring(2, 4);
        // 계산된 년도 숫자 변환
        final int caldYear = Integer.parseInt(caldStrYear);

        LocalDateTime startDateTime = LocalDateTime.of(caldYear, 1, 1, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.now();

        long seconds = Duration.between(startDateTime, endDateTime).getSeconds();
        long randomSeconds = ThreadLocalRandom.current().nextLong(seconds + 1);

        return startDateTime.plusSeconds(randomSeconds);
    }

    // 나이 계산 알고리즘
    public static int calculateAge(LocalDate birthdate, LocalDate currentDate) {
        // 생일이 지났는지 여부 확인
        boolean isBirthdayPassed = birthdate.getDayOfYear() <= currentDate.getDayOfYear();

        // 생일이 지났으면 현재 연도에서 생년월일을 빼고, 그렇지 않으면 현재 연도에서 생년월일을 하나 빼서 계산
        final int age = isBirthdayPassed ? Period.between(birthdate, currentDate).getYears() :
                Period.between(birthdate, currentDate.minusYears(1)).getYears();
        return age;
    }

    // 성별 랜덤 생성 알고리즘
    public static String generateRandomGender() {
        int randomNumber = RANDOM_N.nextInt(2);  // 0 또는 1을 랜덤으로 생성

        return (randomNumber == 0) ? GenderType.MALE.getGender() : GenderType.FEMALE.getGender();
    }

    // 휴대폰 랜덤 생성 알고리즘
    public static String generateRandomPhoneNumber() {

        // 랜덤으로 4자리 숫자 생성
        int firstPart = 1000 + RANDOM_N.nextInt(9000);
        int secondPart = RANDOM_N.nextInt(10000);

        return String.format("010-%04d-%04d", firstPart, secondPart);
    }

    // 메일 생성
    public static String generateMail(final String id) {
        return id + '@' + PROJECT_MAIL;
    }

    // 우편번호 랜덤 생성
    public static String generateRandomPostNo() {
        // 랜덤으로 5자리 숫자 생성
        int randomPostNo = RANDOM_N.nextInt(100000);
        final String randomStrPostNo = Integer.toString(randomPostNo);
        return randomStrPostNo;
    }

    // 기본 주소 랜덤 생성 (시, 구, 동 일치하지 않음)
    public static String generateRandomAddress() {
        String city = CITIES.get(RANDOM_N.nextInt(CITIES.size()));
        String gu = GU_LIST.get(RANDOM_N.nextInt(GU_LIST.size()));
        String dong = DONG_LIST.get(RANDOM_N.nextInt(DONG_LIST.size()));

        final String retAddress = city + " " + gu + " " + dong;
        return retAddress;
    }

    // 상세 주소 랜덤 생성
    public static String generateRandomDetailAddress() {
        String addressSuffix = ADDRESS_SUFFIX_LIST.get(RANDOM_N.nextInt(ADDRESS_SUFFIX_LIST.size()));
        String detailAddress = " " + RANDOM_N.nextInt(100) + "-" + RANDOM_N.nextInt(100);

        final String retDetailAddress = addressSuffix + detailAddress;
        return retDetailAddress;
    }

    // Enum 타입 값 랜덤 생성
    public static <E extends Enum<E>> E generateRandomEnumTypeValue(Class<E> enumClass) {
        List<E> values = Arrays.asList(enumClass.getEnumConstants());
        return values.get(RANDOM_N.nextInt(values.size()));
    }
}

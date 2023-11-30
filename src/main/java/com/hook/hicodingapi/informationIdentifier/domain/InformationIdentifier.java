package com.hook.hicodingapi.informationIdentifier.domain;

import java.util.Arrays;
import java.util.List;

public class InformationIdentifier {
    public static final Integer MAX_PI_NUM = 100;
    public static final List<String> KOREAN_GIVEN_NAMES = Arrays.asList(
            "서영", "민준", "지현", "성우", "미라",
            "지원", "지우", "준호", "예진", "예나",
            "진우", "다은", "태호", "서진", "지은",
            "민서", "지훈", "하은", "윤호", "현서",
            "준영", "서현", "민지", "승우", "윤서",
            "예지", "민혁", "가온", "승민", "하준",
            "윤주", "우진", "가현", "동하", "은서",
            "재원", "윤지", "지율", "태윤", "서윤",
            "준서", "하윤", "태민", "나윤", "우영"
    );

    public static final List<String> KOREAN_SURNAMES = Arrays.asList(
            "김", "이", "박", "최", "정",
            "강", "조", "윤", "장", "임",
            "오", "한", "신", "서", "권",
            "황", "안", "송", "류", "전"
    );

    public static final String PROJECT_MAIL = "hook.com";

    public static final List<String> CITIES = Arrays.asList("서울", "부산", "대구", "인천", "광주", "대전", "울산", "세종", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주");

    public static final List<String> GU_LIST = Arrays.asList("종로구", "중구", "용산구", "성동구", "광진구", "동대문구", "중랑구", "성북구", "강북구", "도봉구",
            "노원구", "은평구", "서대문구", "마포구", "양천구", "강서구", "구로구", "금천구", "영등포구", "동작구", "관악구", "서초구", "강남구", "송파구", "강동구");

    public static final List<String> DONG_LIST = Arrays.asList("가양동", "가회동", "갈월동", "강일동", "개봉동", "공덕동", "공릉동", "관수동", "관음동", "관철동",
            "광희동", "구로동", "구산동", "구수동", "구의동", "군자동", "군자동", "굴피동", "금호동", "남가좌동", "남대문로", "남대문로2가", "남대문로4가", "남대문로5가", "남산동");

    public static final List<String> ADDRESS_SUFFIX_LIST = Arrays.asList("아파트", "빌라", "오피스텔", "상가", "주택");
}

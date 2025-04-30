package com.walab.nanuri.commons.util;

import lombok.Getter;

@Getter
public enum ItemCategory {
    MAJOR_BOOK("전공 서적"),
    GENERAL_BOOK("일반 도서"),
    DIGITAL_DEVICE("디지털기기"),
    STATIONERY("문구류"),
    SPORTS("운동용품"),
    DAILY_SUPPLIES("생활용품"),
    FOOD("식료품"),
    MAN_CLOTHES("남성 의류"),
    WOMAN_CLOTHES("여성 의류"),
    BEAUTY("화장품, 뷰티용품"),
    TICKET_COUPON("티켓, 쿠폰류"),
    PET_SUPPLIES("반려 동물 용품"),
    HOBBY_GOODS("취미 관련"),
    FURNITURE("가구"),
    Electronics("전자 제품"),
    ETC("기타");

    private final String koreanName;

    ItemCategory(String koreanName) {
        this.koreanName = koreanName;
    }
}

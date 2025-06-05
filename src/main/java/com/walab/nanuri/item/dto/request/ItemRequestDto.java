package com.walab.nanuri.item.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
public class ItemRequestDto {
    private String title;
    private String description;
    private String place;
    private String category;
    private LocalDateTime deadline; //달력에서 직접 날짜 선택
    private String deadlineOffsetType; // ex) "내일", "1주일 후", "한달 후" 등
}

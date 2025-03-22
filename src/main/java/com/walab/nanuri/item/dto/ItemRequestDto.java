package com.walab.nanuri.item.dto;

import com.walab.nanuri.item.entity.Item;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemRequestDto {
    private String title;
    private String description;
    private String place;
    private Integer viewCount;
    private String category;
    private Long userId;
    private Boolean isFinished;

//    @Builder
//    public ItemRequestDto(String title, String description, String place, Integer viewCount, String category, Long userId) {
//        this.title = title;
//        this.description = description;
//        this.place = place;
//        this.viewCount = viewCount;
//        this.category = category;
//        this.userId = userId;
//        this.isFinished = false;
//    }

    //DTO -> ENTITY로 변환
    public Item toEntity(){
        return Item.builder()
                .title(this.title)
                .description(this.description)
                .place(this.place)
                .viewCount(this.viewCount)
                .category(this.category)
                .userId(this.userId)
                .isFinished(this.isFinished)
                .build();
    }
}

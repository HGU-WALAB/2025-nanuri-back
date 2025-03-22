package com.walab.nanuri.item.dto;

import com.walab.nanuri.item.entity.Item;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ItemResponseDto {
    private Long id;
    private String title;
    private String description;
    private String place;
    private Integer viewCount;
    private String category;
    private Long userId;
    private Boolean isFinished;
    private String postTime;

//    @Builder
//    public ItemResponseDto(Long id, String title, String description, String place, Integer viewCount,
//                           String category, Long userId, Boolean isFinished, String postTime) {
//        this.id = id;
//        this.title = title;
//        this.description = description;
//        this.place = place;
//        this.viewCount = viewCount;
//        this.category = category;
//        this.userId = userId;
//        this.isFinished = false;
//        this.postTime = postTime;
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

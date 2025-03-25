package com.walab.nanuri.item.dto.response;

import com.walab.nanuri.item.entity.Item;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ItemResponseDto {
    private Long id;
    private String title;
    private Integer viewCount;
    private String category;
    private Boolean isFinished;
    private LocalDateTime createdTime;

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
                .viewCount(this.viewCount)
                .category(this.category)
                .isFinished(this.isFinished)
                .createdTime(this.createdTime)
                .build();
    }
}

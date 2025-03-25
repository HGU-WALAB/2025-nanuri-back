package com.walab.nanuri.item.dto.response;

import com.walab.nanuri.item.entity.Item;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ItemListResponseDto {
    private Long id;
    private String title;
    private Integer viewCount;
    private String category;
    private Boolean isFinished;
    private LocalDateTime createdTime;

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

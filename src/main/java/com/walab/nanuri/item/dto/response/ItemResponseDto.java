package com.walab.nanuri.item.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ItemResponseDto {
    private Long id;
    private String title;
    private String description;
//    private String place;
    private Integer viewCount;
    private String category;
    private Boolean isFinished;
    private LocalDateTime createdTime;
    private Integer wishCount;
    private Boolean isOwner;

//    public Item toEntity(){
//        return Item.builder()
//                .id(this.id)
//                .title(this.title)
//                .description(this.description)
//                .viewCount(this.viewCount)
//                .category(this.category)
//                .isFinished(this.isFinished)
//                .createdTime(this.createdTime)
//                .build();
//    }

}

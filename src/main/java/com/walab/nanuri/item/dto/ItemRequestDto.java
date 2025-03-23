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


    //DTO -> ENTITY로 변환
    public Item toEntity(){
        return Item.builder()
                .title(this.title)
                .description(this.description)
                .place(this.place)
                .viewCount(this.viewCount)
                .category(this.category)
                .build();
    }
}

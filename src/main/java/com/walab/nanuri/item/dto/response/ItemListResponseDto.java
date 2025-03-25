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
    private LocalDateTime createdTime;

    //DTO -> ENTITY로 변환
    public Item toEntity(){
        return Item.builder()
                .title(this.title)
                .createdTime(this.createdTime)
                .build();
    }
}

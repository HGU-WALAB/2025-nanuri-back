package com.walab.nanuri.item.dto.request;

import com.walab.nanuri.item.entity.Item;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ItemRequestDto {
    private String title;
    private String description;
    private String place;
    private String category;


    //DTO -> ENTITY로 변환
    public Item toEntity(){
        return Item.builder()
                .title(this.title)
                .description(this.description)
                .place(this.place)
                .category(this.category)
                .build();
    }
}

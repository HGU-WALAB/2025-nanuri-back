package com.walab.nanuri.item.dto.response;

import com.walab.nanuri.commons.util.Time;
import com.walab.nanuri.item.entity.Item;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ItemListResponseDto {
    private Long itemId;
    private String title;
    private String description;
    private String category;
    private String image;
    private Boolean isFinished;
    private String createdTime;
    private String modifiedTime;

    public static ItemListResponseDto from(Item item, String image) {
        return ItemListResponseDto.builder()
                .itemId(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .image(image)
                .isFinished(item.getIsFinished())
                .createdTime(item.getCreatedTime().toString())
                .modifiedTime(item.getModifiedTime().toString())
                .build();
    }
}

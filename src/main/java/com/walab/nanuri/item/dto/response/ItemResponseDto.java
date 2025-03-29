package com.walab.nanuri.item.dto.response;

import com.walab.nanuri.commons.util.Time;
import com.walab.nanuri.item.entity.Item;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {
    private Long id;
    private String title;
    private String description;
    private Integer viewCount;
    private String category;
    private Boolean isFinished;
    private String createdTime;
    private Integer wishCount;
    private List<String> images;
    private Boolean isOwner;

    public static ItemResponseDto from(Item item, List<String> images, Boolean isOwner) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .viewCount(item.getViewCount())
                .category(item.getCategory())
                .createdTime(Time.calculateTime(Timestamp.valueOf(item.getCreatedTime())))
                .wishCount(item.getWishCount())
                .isFinished(item.getIsFinished())
                .images(images)
                .isOwner(isOwner)
                .build();
    }
}

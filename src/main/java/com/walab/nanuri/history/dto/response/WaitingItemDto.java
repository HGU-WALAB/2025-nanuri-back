package com.walab.nanuri.history.dto.response;

import com.walab.nanuri.commons.util.Time;
import com.walab.nanuri.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaitingItemDto {
    private Long historyId;
    private Long itemId;
    private String title;
    private String description;
    private String category;
    private String image;
    private String createdTime;
    private String updatedTime;



    public static WaitingItemDto from(Item item, Long historyId, String image){
        return WaitingItemDto.builder()
                .historyId(historyId)
                .itemId(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .image(image)
                .createdTime(Time.calculateTime(Timestamp.valueOf(item.getCreatedTime())))
                .updatedTime(item.getUpdatedTime().toString())
                .build();
    }
}

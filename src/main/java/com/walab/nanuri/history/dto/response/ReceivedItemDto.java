package com.walab.nanuri.history.dto.response;

import com.walab.nanuri.commons.util.Time;
import com.walab.nanuri.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivedItemDto {
    private Long historyId;
    private Long itemId;
    private String title;
    private String description;
    private String category;
    private String image;
    private String createdTime;
    private String modifiedTime;



    public static ReceivedItemDto from(Item item, Long historyId, String image){
        return ReceivedItemDto.builder()
                .historyId(historyId)
                .itemId(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory().getKoreanName())
                .image(image)
                .createdTime(item.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedTime(item.getModifiedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}

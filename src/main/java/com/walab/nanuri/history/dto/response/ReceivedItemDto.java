package com.walab.nanuri.history.dto.response;

import com.walab.nanuri.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceivedItemDto {
    private Long itemId;
    private String title;
    private LocalDateTime createdTime;

    public static ReceivedItemDto from(Item item){
        return ReceivedItemDto.builder()
                .itemId(item.getId())
                .title(item.getTitle())
                .createdTime(item.getCreatedTime())
                .build();
    }
}

package com.walab.nanuri.item.dto.response;

import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.user.dto.UserDto;
import com.walab.nanuri.user.dto.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ItemListResponseDto {
    private Long itemId;
    private String title;
    private LocalDateTime createdTime;

    public static ItemListResponseDto from(Item item) {
        return ItemListResponseDto.builder()
                .itemId(item.getId())
                .title(item.getTitle())
                .createdTime(item.getCreatedTime())
                .build();
    }
}

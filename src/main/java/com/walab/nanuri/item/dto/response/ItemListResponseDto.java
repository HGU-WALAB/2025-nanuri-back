package com.walab.nanuri.item.dto.response;

import com.walab.nanuri.commons.util.Time;
import com.walab.nanuri.image.dto.response.ImageResponseDto;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.user.dto.UserDto;
import com.walab.nanuri.user.dto.UserResponseDto;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ItemListResponseDto {
    private Long itemId;
    private String title;
    private String image;
    private String createdTime;

    public static ItemListResponseDto from(Item item, String image) {
        return ItemListResponseDto.builder()
                .itemId(item.getId())
                .title(item.getTitle())
                .image(image)
                .createdTime(Time.calculateTime(Timestamp.valueOf(item.getCreatedTime())))
                .build();
    }
}

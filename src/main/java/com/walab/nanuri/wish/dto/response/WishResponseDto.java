package com.walab.nanuri.wish.dto.response;

import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.wish.entity.Wish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishResponseDto {
    private Long id;
    private String title;
    private LocalDateTime createdTime;

    public static WishResponseDto createDefaultDto(Wish wish, Item item) {
        return WishResponseDto.builder()
                .id(wish.getId())
                .title(item.getTitle())
                .createdTime(item.getCreatedTime())
                .build();
    }
}

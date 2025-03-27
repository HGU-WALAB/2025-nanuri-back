package com.walab.nanuri.wish.dto.response;

import com.walab.nanuri.item.dto.response.ItemListResponseDto;
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
    private Long wishId;
    private ItemListResponseDto itemListResponseDto;

    public static WishResponseDto createDefaultDto(Wish wish, Item item) {
        return WishResponseDto.builder()
                .wishId(wish.getId())
                .itemListResponseDto(ItemListResponseDto.from(item))
                .build();
    }
}

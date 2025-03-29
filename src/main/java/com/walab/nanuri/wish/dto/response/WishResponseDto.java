package com.walab.nanuri.wish.dto.response;

import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.wish.entity.Wish;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishResponseDto {
    private Long wishId;
    private Long itemId;
    private String title;
    private String description;
    private String category;
    private String imageUrl;

    public static WishResponseDto from(Wish wish, ItemListResponseDto itemDto) {
        return WishResponseDto.builder()
                .wishId(wish.getId())
                .itemId(itemDto.getItemId())
                .title(itemDto.getTitle())
                .description(itemDto.getDescription())
                .category(itemDto.getCategory())
                .imageUrl(itemDto.getImage())
                .build();
    }
}

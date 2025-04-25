package com.walab.nanuri.wish.dto.response;

import com.walab.nanuri.commons.util.ShareStatus;
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
    private String nickname;
    private String title;
    private String description;
    private String category;
    private String imageUrl;
    private ShareStatus shareStatus;
    private Boolean wishStatus;
    private Integer wishCount;
    private Integer chatCount;
    private Integer viewCount;
    private String createdTime;
    private String modifiedTime;

    public static WishResponseDto from(Wish wish, ItemListResponseDto itemDto) {
        return WishResponseDto.builder()
                .wishId(wish.getId())
                .itemId(itemDto.getItemId())
                .nickname(itemDto.getNickname())
                .title(itemDto.getTitle())
                .description(itemDto.getDescription())
                .category(itemDto.getCategory())
                .imageUrl(itemDto.getImage())
                .shareStatus(itemDto.getShareStatus())
                .wishStatus(itemDto.getWishStatus())
                .wishCount(itemDto.getWishCount())
                .chatCount(itemDto.getChatCount())
                .viewCount(itemDto.getViewCount())
                .createdTime(itemDto.getCreatedTime())
                .modifiedTime(itemDto.getModifiedTime())
                .build();
    }
}

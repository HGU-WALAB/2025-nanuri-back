package com.walab.nanuri.item.dto.response;

import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.item.entity.Item;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@AllArgsConstructor
public class ItemListResponseDto {
    private Long itemId;
    private String title;
    private String nickname;
    private String description;
    private String category;
    private String image;
    private ShareStatus shareStatus;
    private String createdTime;
    private String modifiedTime;
    private Integer wishCount;
    private Integer chatCount;


    public static ItemListResponseDto from(Item item, String image, String nickname) {
        return ItemListResponseDto.builder()
                .itemId(item.getId())
                .nickname(nickname)
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .image(image)
                .shareStatus(item.getShareStatus())
                .createdTime(item.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedTime(item.getModifiedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    public static ItemListResponseDto from(Item item, String image, String nickname, Integer wishCount, Integer chatCount) {
        return ItemListResponseDto.builder()
                .itemId(item.getId())
                .nickname(nickname)
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .image(image)
                .shareStatus(item.getShareStatus())
                .createdTime(item.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedTime(item.getModifiedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .wishCount(item.getWishCount() != null ? item.getWishCount() : 0)
                .chatCount(item.getChatCount() != null ? item.getChatCount() : 0)
                .build();
    }

}

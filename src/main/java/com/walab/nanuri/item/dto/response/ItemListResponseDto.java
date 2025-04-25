package com.walab.nanuri.item.dto.response;

import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.item.entity.Item;
import lombok.*;

import java.time.LocalDateTime;
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
    private Boolean wishStatus;
    private String createdTime;
    private String modifiedTime;
    private Integer wishCount;
    private Integer chatCount;
    private Integer viewCount;
    private LocalDateTime deadline;

    public static ItemListResponseDto from(Item item, String image, String nickname, Boolean wishStatus) {
        return ItemListResponseDto.builder()
                .itemId(item.getId())
                .nickname(nickname)
                .title(item.getTitle())
                .description(item.getDescription())
                .category(item.getCategory())
                .image(image)
                .shareStatus(item.getShareStatus())
                .wishStatus(wishStatus)
                .createdTime(item.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .modifiedTime(item.getModifiedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .wishCount(item.getWishCount())
                .chatCount(item.getChatCount())
                .viewCount(item.getViewCount())
                .deadline(item.getDeadline())
                .build();
    }
}

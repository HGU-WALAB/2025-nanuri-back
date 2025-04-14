package com.walab.nanuri.item.dto.response;

import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.item.entity.Item;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDto {
    private Long id;
    private String nickname;
    private String title;
    private String description;
    private Integer viewCount;
    private String category;
    private ShareStatus shareStatus;
    private String createdTime;
    private Integer wishCount;
    private Integer chatCount;
    private List<String> images;
    private Boolean isOwner;

    public static ItemResponseDto from(Item item, List<String> images, Boolean isOwner, String nickname) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .nickname(nickname)
                .title(item.getTitle())
                .description(item.getDescription())
                .viewCount(item.getViewCount())
                .category(item.getCategory())
                .createdTime(item.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .wishCount(item.getWishCount())
                .chatCount(item.getChatCount())
                .shareStatus(item.getShareStatus())
                .images(images)
                .isOwner(isOwner)
                .build();
    }
}

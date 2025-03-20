package com.walab.nanuri.item.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ItemResponseDto {
    private Long id;
    private String title;
    private String description;
    private String place;
    private Integer viewCount;
    private String category;
    private Long userId;
    private Boolean isFinished;
    private LocalDateTime createdAt;

    @Builder
    public ItemResponseDto(String title, String description, String place, Integer viewCount,
                           String category, Long userId, Boolean isFinished, LocalDateTime createdAt) {
        this.title = title;
        this.description = description;
        this.place = place;
        this.viewCount = viewCount;
        this.category = category;
        this.userId = userId;
        this.isFinished = false;
        this.createdAt = LocalDateTime.now();
    }
}

package com.walab.nanuri.item.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
public class ItemRequestDto {
    private String title;
    private String description;
    private String place;
    private String category;
    private LocalDateTime deadline;
}

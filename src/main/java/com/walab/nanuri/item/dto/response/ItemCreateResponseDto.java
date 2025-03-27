package com.walab.nanuri.item.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class ItemCreateResponseDto {
    private Long itemId;
}

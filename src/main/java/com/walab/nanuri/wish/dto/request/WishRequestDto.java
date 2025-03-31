package com.walab.nanuri.wish.dto.request;

import lombok.Getter;

public class WishRequestDto {

    @Getter
    public static class WishCreateRequestDto {
        private Long itemId;
    }
}

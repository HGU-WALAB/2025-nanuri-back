package com.walab.nanuri.chat.dto.request;


import lombok.Getter;

@Getter
public class ChatMessageRequestDto {
    private String roomId;
    private String message;
}
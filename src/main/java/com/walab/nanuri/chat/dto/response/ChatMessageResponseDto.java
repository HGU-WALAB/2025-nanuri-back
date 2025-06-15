package com.walab.nanuri.chat.dto.response;

import com.walab.nanuri.chat.entity.ChatMessage;
import com.walab.nanuri.user.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private String roomId;
    private String senderNickname;
    private String receiverNickname;
    private LocalDateTime createdAt;
    private ChatMessage.MessageType type;
    private String message;

    public static ChatMessageResponseDto from(ChatMessage chatMessage, User sender, User receiver) {
        return ChatMessageResponseDto.builder()
                .roomId(chatMessage.getRoomId())
                .senderNickname(sender != null ? sender.getNickname() : "null")
                .receiverNickname(receiver != null ? receiver.getNickname() : "null")
                .createdAt(chatMessage.getTimestamp())
                .type(chatMessage.getType())
                .message(chatMessage.getMessage())
                .build();
    }
}

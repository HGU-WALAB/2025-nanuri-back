package com.walab.nanuri.chat.dto.response;

import com.walab.nanuri.chat.entity.ChatMessage;
import com.walab.nanuri.user.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponseDto {
    private String roomId;
    private String senderName;
    private String receiverName;
    private String message;
    private boolean isMine;

    public static ChatMessageResponseDto from(ChatMessage chatMessage, String uniqueId, User sender, User receiver) {
        return ChatMessageResponseDto.builder()
                .roomId(chatMessage.getRoomId())
                .senderName(sender.getName())
                .receiverName(receiver.getName())
                .message(chatMessage.getMessage())
                .isMine(uniqueId.equals(chatMessage.getSenderId()))
                .build();
    }
}

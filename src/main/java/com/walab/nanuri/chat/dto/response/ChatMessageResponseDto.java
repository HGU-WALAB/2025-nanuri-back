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
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private String message;
    private boolean isMine;

    public static ChatMessageResponseDto from(ChatMessage chatMessage, String uniqueId, User sender, User receiver) {
        return ChatMessageResponseDto.builder()
                .roomId(chatMessage.getRoomId())
                .senderId(chatMessage.getSenderId())
                .senderName(sender.getName())
                .receiverId(chatMessage.getReceiverId())
                .receiverName(receiver.getName())
                .message(chatMessage.getMessage())
                .isMine(uniqueId.equals(chatMessage.getSenderId()))
                .build();
    }
}

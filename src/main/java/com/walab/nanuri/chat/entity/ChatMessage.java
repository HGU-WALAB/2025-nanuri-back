package com.walab.nanuri.chat.entity;

import com.walab.nanuri.chat.dto.request.ChatMessageRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat_messages")
public class ChatMessage {
    public enum MessageType {
        ENTER, LEAVE, TALK
    }

    @Id
    private String id;
    private String roomId;
    private String senderId;
    private String receiverId;
    private MessageType type;
    private String roomKey;
    private String message;
    private LocalDateTime timestamp;

    private ChatMessage(String roomId, String senderId, String receiverId, String message, String roomKey, LocalDateTime now, MessageType type) {
        this.roomId = roomId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.roomKey = roomKey;
        this.timestamp = now;
        this.type = type;
    }

    public static ChatMessage fromDto(ChatMessageRequestDto dto, String senderId,String receiverId, String roomKey, MessageType type) {
        return new ChatMessage(
                dto.getRoomId(),
                senderId,
                receiverId,
                dto.getMessage(),
                roomKey,
                LocalDateTime.now(),
                type
        );
    }
}

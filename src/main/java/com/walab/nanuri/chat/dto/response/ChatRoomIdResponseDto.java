package com.walab.nanuri.chat.dto.response;

import com.walab.nanuri.chat.entity.ChatRoom;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomIdResponseDto {
    private String roomId;

    public static ChatRoomIdResponseDto fromEntity(ChatRoom chatRoom) {
        return ChatRoomIdResponseDto.builder()
                .roomId(String.valueOf(chatRoom.getId()))
                .build();
    }
}

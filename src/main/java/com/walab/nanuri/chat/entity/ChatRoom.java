package com.walab.nanuri.chat.entity;

import com.walab.nanuri.chat.dto.request.ChatRoomRequestDto;
import com.walab.nanuri.commons.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatRoom extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long itemId;
    private Long historyId;
    private String sellerId;
    private String receiverId;
    private String roomKey;

    public static String createRoomKey(String itemId, String sellerId, String receiverId) {
        return itemId + "_" + (sellerId.compareTo(receiverId) < 0 ? sellerId + "_" + receiverId : receiverId + "_" + sellerId);
    }

    public static ChatRoom toEntity(ChatRoomRequestDto.CreateChatRoomRequest dto) {
        return ChatRoom.builder()
                .sellerId(dto.getSellerId())
                .itemId(dto.getItemId())
                .receiverId(dto.getReceiverId())
                .build();
    }
}
package com.walab.nanuri.chat.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

public class ChatRoomRequestDto {

    @Getter
    @Builder
    public static class CreateChatRoomRequest{
        private final Long itemId;
        private final Long historyId;
        private final String sellerId;
        private final String receiverId;

        public boolean checkChatRoomUserId(String userId) {
            return Objects.equals(sellerId, userId) || Objects.equals(receiverId, userId);
        }
    }

    @Getter
    @Builder
    public static class ChatRoomUserValidationRequest{
        private final String sellerId;
        private final String receiverId;

        public boolean checkChatRoomUserId(String userId) {
            return Objects.equals(sellerId, userId) || Objects.equals(receiverId, userId);
        }
    }
}

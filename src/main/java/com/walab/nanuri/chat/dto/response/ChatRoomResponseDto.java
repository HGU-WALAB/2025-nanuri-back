package com.walab.nanuri.chat.dto.response;

import com.walab.nanuri.chat.entity.ChatRoom;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponseDto {
    private Long roomId;
    private Long itemId;
    private String itemTitle;
    private String itemImage;
    private String opponentId;
    private String opponentName;
    private boolean isSeller;

    public static ChatRoomResponseDto from(ChatRoom room, String myId, User opponent, Item item, String ItemUrl) {
        boolean isSeller = room.getSellerId().equals(myId);
        String opponentId = isSeller ? room.getReceiverId() : room.getSellerId();

        return ChatRoomResponseDto.builder()
                .roomId(room.getId())
                .itemId(room.getItemId())
                .opponentId(opponentId)
                .opponentName(opponent.getName())
                .itemTitle(item.getTitle())
                .itemImage(ItemUrl)
                .isSeller(isSeller)
                .build();
    }
}
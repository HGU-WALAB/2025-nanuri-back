package com.walab.nanuri.chat.dto.response;

import com.walab.nanuri.chat.entity.ChatRoom;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.want.entity.WantPost;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponseDto {
    private Long roomId;
    private Long itemId;
    private Long postId;
    private String title;
    private String itemImage;
    private String opponentName;
    private boolean isSeller;

    public static ChatRoomResponseDto from(ChatRoom room, String myId, User opponent, Item item, String ItemUrl, WantPost post) {
        boolean isSeller = room.getSellerId().equals(myId);

        return ChatRoomResponseDto.builder()
                .roomId(room.getId())
                .itemId(room.getItemId())
                .postId(room.getPostId())
                .opponentName(opponent.getName())
                .title(item == null ? post.getTitle() : item.getTitle())
                .itemImage(ItemUrl)
                .isSeller(isSeller)
                .build();
    }
}
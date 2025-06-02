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
    private String opponentNickname;

    public static ChatRoomResponseDto from(ChatRoom room, String opponentNickname, Item item, String ItemUrl, WantPost post) {

        return ChatRoomResponseDto.builder()
                .roomId(room.getId())
                .itemId(room.getItemId())
                .postId(room.getPostId())
                .opponentNickname(opponentNickname)
                .title(room.getTitle())
                .itemImage(ItemUrl)
                .build();
    }
}
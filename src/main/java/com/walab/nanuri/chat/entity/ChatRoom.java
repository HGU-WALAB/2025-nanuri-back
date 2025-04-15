package com.walab.nanuri.chat.entity;

import com.walab.nanuri.commons.entity.BaseTimeEntity;
import com.walab.nanuri.commons.util.PostType;
import jakarta.persistence.*;
import lombok.*;

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
    private Long postId;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Setter
    private String sellerId;

    @Setter
    private String receiverId;


    private String roomKey;

    public static String createRoomKey(String itemId, String sellerId, String receiverId) {
        return itemId + "_" + (sellerId.compareTo(receiverId) < 0 ? sellerId + "_" + receiverId : receiverId + "_" + sellerId);
    }
}
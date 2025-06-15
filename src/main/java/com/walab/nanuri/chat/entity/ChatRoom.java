package com.walab.nanuri.chat.entity;

import com.walab.nanuri.commons.entity.BaseTimeEntity;
import com.walab.nanuri.commons.util.PostType;
import javax.persistence.*;
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
    private String title;
    private Long postId;

    @Enumerated(EnumType.STRING)
    private PostType postType;


    private String roomKey;

    public static String createRoomKey(String itemId, String userId1, String userId2) {
        return itemId + "_" + (userId1.compareTo(userId2) < 0 ? userId1 + "_" + userId2 : userId2 + "_" + userId1);
    }
}
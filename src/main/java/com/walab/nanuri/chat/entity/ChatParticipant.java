package com.walab.nanuri.chat.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "chat_participant")
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomKey;
    private String userId;

    @Setter
    private Boolean hasLeft;

    @Setter
    private LocalDateTime leftAt;

    @Setter
    private LocalDateTime lastReadAt;

    // 연관관계 설정 (옵션)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;


    public static ChatParticipant of(ChatRoom chatRoom, String userId) {
        return ChatParticipant.builder()
                .chatRoom(chatRoom)
                .roomKey(chatRoom.getRoomKey())
                .userId(userId)
                .hasLeft(false)
                .lastReadAt(LocalDateTime.now())
                .build();
    }
}
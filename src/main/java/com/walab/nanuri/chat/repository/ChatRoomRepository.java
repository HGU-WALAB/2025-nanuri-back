package com.walab.nanuri.chat.repository;

import com.walab.nanuri.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByItemId(Long itemId);
    ChatRoom findByRoomKey(String roomKey);
}

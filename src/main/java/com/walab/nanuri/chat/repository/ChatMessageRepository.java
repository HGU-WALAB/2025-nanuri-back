package com.walab.nanuri.chat.repository;

import com.walab.nanuri.chat.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findTop40ByRoomKeyOrderByTimestampDesc(String roomId);
    List<ChatMessage> findTop40ByRoomKeyAndTimestampLessThanOrderByTimestampDesc(String roomId, LocalDateTime timestamp);

    void deleteByRoomKey(String roomKey);

    void deleteByRoomIdIn(List<String> roomIds);
}

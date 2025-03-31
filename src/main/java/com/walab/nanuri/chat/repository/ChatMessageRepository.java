package com.walab.nanuri.chat.repository;

import com.walab.nanuri.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findTop40ByRoomIdOrderByTimestampDesc(String roomId);
    List<ChatMessage> findTop40ByRoomIdAndTimestampLessThanOrderByTimestampDesc(String roomId, LocalDateTime timestamp);

    void deleteByRoomKey(String roomKey);
}

package com.walab.nanuri.chat.repository;

import com.walab.nanuri.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    boolean existsBySellerIdAndReceiverId(String sellerId, String receiverId);

    List<ChatRoom> findBySellerIdOrReceiverIdOrderByModifiedTimeDesc(String sellerId, String receiverId);
    int countByItemId(Long itemId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END " +
            "FROM ChatRoom c WHERE c.sellerId = :userId OR c.receiverId = :userId")
    boolean existsByUserInChatRoom(@Param("userId") String userId);

    List<ChatRoom> findByItemId(Long itemId);
}

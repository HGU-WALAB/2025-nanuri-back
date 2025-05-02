package com.walab.nanuri.chat.repository;

import com.walab.nanuri.chat.entity.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    // 특정 유저가 특정 채팅방에 참여 중인지 조회
    Optional<ChatParticipant> findByRoomKeyAndUserId(String roomKey, String userId);

    // 특정 채팅방의 전체 참여자 조회
    List<ChatParticipant> findByRoomKey(String roomKey);

    // 아직 방을 나가지 않은 참여자 목록
    List<ChatParticipant> findByRoomKeyAndHasLeftFalse(String roomKey);

    void deleteAllByRoomKey(String roomKey);

    // 유저가 참여 중인 전체 채팅방 조회
    List<ChatParticipant> findByUserIdAndHasLeftFalse(String userId);

    boolean existsByRoomKeyAndUserIdAndHasLeftFalse(String roomKey, String userId);
}
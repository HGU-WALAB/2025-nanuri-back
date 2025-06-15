package com.walab.nanuri.chat.service;

import com.walab.nanuri.chat.entity.ChatParticipant;
import com.walab.nanuri.chat.entity.ChatRoom;
import com.walab.nanuri.chat.repository.ChatMessageRepository;
import com.walab.nanuri.chat.repository.ChatParticipantRepository;
import com.walab.nanuri.chat.repository.ChatRoomRepository;
import com.walab.nanuri.chat.service.implement.ChatParticipantServiceImpl;
import com.walab.nanuri.commons.exception.CustomException;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.walab.nanuri.commons.exception.ErrorCode.CHATROOM_NOT_FOUND;
import static com.walab.nanuri.commons.exception.ErrorCode.PARTICIPANT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatParticipantService implements ChatParticipantServiceImpl {
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageService chatMessageService;

    public void enterRoom(ChatRoom chatRoom, String userId){
        chatParticipantRepository.save(ChatParticipant.of(chatRoom, userId));
    }

    @Transactional
    public void leaveRoom(Long roomId, String userId) {
        String roomKey = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND))
                .getRoomKey();

        ChatParticipant participant = chatParticipantRepository
                .findByRoomKeyAndUserId(roomKey, userId)
                .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));

        participant.setHasLeft(true);
        participant.setLeftAt(LocalDateTime.now());
        chatParticipantRepository.save(participant);

        boolean allLeft = chatParticipantRepository
                .findByRoomKeyAndHasLeftFalse(roomKey)
                .isEmpty();

        ChatRoom chatRoom = chatRoomRepository
                .findByRoomKey(roomKey);
        if (allLeft) {
            chatMessageRepository.deleteByRoomKey(roomKey);
            chatParticipantRepository.deleteAllByRoomKey(roomKey);
            chatRoomRepository.delete(chatRoom);
        } else {
            chatMessageService.handleExit(chatRoom, userId);
        }
    }

    public ChatParticipant getChatParticipant(String roomKey , String userId) {
        return chatParticipantRepository.findByRoomKeyAndUserId(roomKey, userId).orElse(null);
    }

}

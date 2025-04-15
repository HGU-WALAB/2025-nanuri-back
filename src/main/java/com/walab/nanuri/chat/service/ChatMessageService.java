package com.walab.nanuri.chat.service;

import com.walab.nanuri.chat.dto.request.ChatMessageRequestDto;
import com.walab.nanuri.chat.dto.response.ChatMessageResponseDto;
import com.walab.nanuri.chat.entity.ChatMessage;
import com.walab.nanuri.chat.entity.ChatRoom;
import com.walab.nanuri.chat.repository.ChatMessageRepository;
import com.walab.nanuri.chat.repository.ChatRoomRepository;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.commons.exception.ErrorCode;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveAndSend(ChatMessageRequestDto request) {
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(request.getRoomId()))
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
        String receiverId = Objects.equals(chatRoom.getSellerId(), request.getSenderId()) ? chatRoom.getReceiverId() : chatRoom.getSellerId();
        String roomKey = chatRoom.getRoomKey();

        ChatMessage message = ChatMessage.fromDto(request, receiverId, roomKey);
        chatMessageRepository.save(message);

        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        ChatMessageResponseDto response = ChatMessageResponseDto.from(message, sender, receiver);

        messagingTemplate.convertAndSend("/sub/chat/room/" + request.getRoomId(), response);
    }

    public List<ChatMessageResponseDto> getChatMessages(String uniqueId,
                                                       Long roomId,
                                                       LocalDateTime timestamp) {
        if (!chatRoomRepository.existsById(roomId)) {
            throw new CustomException(ErrorCode.CHATROOM_NOT_FOUND);
        }

        String stringRoomId = String.valueOf(roomId);

        List<ChatMessage> messages = (timestamp == null) ?
                chatMessageRepository.findTop40ByRoomIdOrderByTimestampDesc(stringRoomId) :
                chatMessageRepository.findTop40ByRoomIdAndTimestampLessThanOrderByTimestampDesc(stringRoomId, timestamp);

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
        String receiverId = Objects.equals(chatRoom.getSellerId(), uniqueId) ? chatRoom.getReceiverId() : chatRoom.getSellerId();

        User sender = userRepository.findById(uniqueId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return messages.stream()
                .map(message -> ChatMessageResponseDto.from(message, sender, receiver))
                .collect(Collectors.toList());
    }
}

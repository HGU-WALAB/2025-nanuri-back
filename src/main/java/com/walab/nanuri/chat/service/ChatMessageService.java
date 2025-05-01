package com.walab.nanuri.chat.service;

import com.walab.nanuri.chat.dto.request.ChatMessageRequestDto;
import com.walab.nanuri.chat.dto.response.ChatMessageResponseDto;
import com.walab.nanuri.chat.entity.ChatMessage;
import com.walab.nanuri.chat.entity.ChatRoom;
import com.walab.nanuri.chat.repository.ChatMessageRepository;
import com.walab.nanuri.chat.repository.ChatParticipantRepository;
import com.walab.nanuri.chat.repository.ChatRoomRepository;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.commons.exception.ErrorCode;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import com.walab.nanuri.chat.entity.ChatParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatParticipantRepository chatParticipantRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveAndSend(ChatMessageRequestDto request) {
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(request.getRoomId()))
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));
        User sender = userRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String roomKey = chatRoom.getRoomKey();

        List<ChatParticipant> participants = chatParticipantRepository.findByRoomKeyAndHasLeftFalse(roomKey);

        if (participants.size() != 2) {
            throw new CustomException(ErrorCode.VALID_USER);
        }

        String senderId = sender.getUniqueId();
        String receiverId = participants.stream()
                .map(ChatParticipant::getUserId)
                .filter(id -> !id.equals(senderId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.PARTICIPANT_NOT_FOUND));

        ChatMessage message = ChatMessage.fromDto(request, sender.getUniqueId(), receiverId, roomKey);
        chatMessageRepository.save(message);

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

        String roomKey = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND))
                .getRoomKey();

        if (!chatParticipantRepository.existsByRoomKeyAndUserIdAndHasLeftFalse(roomKey,  uniqueId)) {
            throw new CustomException(ErrorCode.VALID_USER);
        }

        String stringRoomId = String.valueOf(roomId);

        List<ChatMessage> messages = (timestamp == null) ?
                chatMessageRepository.findTop40ByRoomKeyOrderByTimestampDesc(roomKey) :
                chatMessageRepository.findTop40ByRoomKeyAndTimestampLessThanOrderByTimestampDesc(roomKey, timestamp);

        Set<String> userIds = messages.stream()
                .flatMap(msg -> Stream.of(msg.getSenderId(), msg.getReceiverId()))
                .collect(Collectors.toSet());

        Map<String, User> userMap = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(User::getUniqueId, Function.identity()));

        return messages.stream()
                .map(message -> {
                    User sender = userMap.get(message.getSenderId());
                    User receiver = userMap.get(message.getReceiverId());

                    if (sender == null || receiver == null) {
                        throw new CustomException(ErrorCode.USER_NOT_FOUND);
                    }

                    return ChatMessageResponseDto.from(message, sender, receiver);
                })
                .collect(Collectors.toList());
    }
}

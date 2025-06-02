package com.walab.nanuri.chat.service;

import com.walab.nanuri.chat.dto.response.ChatRoomResponseDto;
import com.walab.nanuri.chat.entity.ChatParticipant;
import com.walab.nanuri.chat.entity.ChatRoom;
import com.walab.nanuri.chat.repository.ChatMessageRepository;
import com.walab.nanuri.chat.repository.ChatParticipantRepository;
import com.walab.nanuri.chat.repository.ChatRoomRepository;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.image.repository.ImageRepository;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import com.walab.nanuri.want.entity.WantPost;
import com.walab.nanuri.want.repository.WantPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.walab.nanuri.commons.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ItemRepository itemRepository;
    private final WantPostRepository wantPostRepository;

    public ChatRoomResponseDto getChatRoom(String myId, Long chatRoomId) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND));

        ChatParticipant participant = chatParticipantRepository.findByRoomKeyAndUserId(room.getRoomKey(), myId)
                .orElseThrow(() -> new CustomException(PARTICIPANT_NOT_FOUND));

        return toChatRoomResponse(participant, myId);
    }

    public List<ChatRoomResponseDto> getChatRooms(String myId) {
        List<ChatParticipant> chatParticipants = chatParticipantRepository.findByUserIdAndHasLeftFalse(myId);

        return chatParticipants.stream()
                .map(participant -> toChatRoomResponse(participant, myId))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteChatRoomsByItemId(Long itemId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByItemId(itemId);

        List<String> roomIds = chatRooms.stream()
                .map(chatRoom -> String.valueOf(chatRoom.getId()))
                .collect(Collectors.toList());

        chatMessageRepository.deleteByRoomIdIn(roomIds);

        chatRoomRepository.deleteAll(chatRooms);
    }

    private ChatRoomResponseDto toChatRoomResponse(ChatParticipant participant, String myId) {
        ChatRoom room = participant.getChatRoom();

        String opponentId = chatParticipantRepository.findByRoomKey(room.getRoomKey()).stream()
                .map(ChatParticipant::getUserId)
                .filter(id -> !id.equals(myId))
                .findFirst()
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        User opponent = userRepository.findById(opponentId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Item item = null;
        String image = null;
        if (room.getItemId() != null) {
            item = itemRepository.findById(room.getItemId())
                    .orElse(null);
            if (item != null) {
                image = imageRepository.findTopByItemIdOrderByIdAsc(room.getItemId()).getFileUrl();
            }
        }

        WantPost post = null;
        if (room.getPostId() != null) {
            post = wantPostRepository.findById(room.getPostId())
                    .orElse(null);
        }

        return ChatRoomResponseDto.from(room, opponent.getNickname(), item, image, post);
    }
}

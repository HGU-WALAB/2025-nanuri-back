package com.walab.nanuri.chat.service;

import com.walab.nanuri.chat.dto.response.ChatRoomResponseDto;
import com.walab.nanuri.chat.entity.ChatRoom;
import com.walab.nanuri.chat.repository.ChatMessageRepository;
import com.walab.nanuri.chat.repository.ChatRoomRepository;

import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.commons.exception.ErrorCode;
import com.walab.nanuri.image.repository.ImageRepository;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ItemRepository itemRepository;

    public List<ChatRoomResponseDto> getChatRooms(String myId){
        List<ChatRoom> chatRooms = chatRoomRepository.findBySellerIdOrReceiverIdOrderByLastModifiedDesc(myId, myId);

        return chatRooms.stream()
                .map(room ->{
                    String opponentId = room.getSellerId().equals(myId) ? room.getReceiverId() : room.getSellerId();
                    User opponent = userRepository.findById(opponentId)
                            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

                    Item item = itemRepository.findById(room.getItemId())
                            .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));

                    String image = imageRepository.findTopByItemIdOrderByIdAsc(room.getItemId()).getFileUrl();

                    return ChatRoomResponseDto.from(room, myId, opponent, item, image);
                    }
                ).collect(Collectors.toList());
    }

    public void deleteChatRoom(String myId, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHATROOM_NOT_FOUND));

        if(!Objects.equals(chatRoom.getSellerId(), myId)) {
            throw new CustomException(ErrorCode.VALID_DELETE_CHATROOM);
        }

        chatMessageRepository.deleteByRoomKey(chatRoom.getRoomKey());

        chatRoomRepository.delete(chatRoom);
    }
}

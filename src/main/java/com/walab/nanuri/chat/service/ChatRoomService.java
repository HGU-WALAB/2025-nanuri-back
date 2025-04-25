package com.walab.nanuri.chat.service;

import com.walab.nanuri.chat.dto.response.ChatRoomResponseDto;
import com.walab.nanuri.chat.entity.ChatRoom;
import com.walab.nanuri.chat.repository.ChatMessageRepository;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.walab.nanuri.commons.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ItemRepository itemRepository;
    private final WantPostRepository wantPostRepository;

    public ChatRoomResponseDto getChatRoom(String myId, Long chatRoomId) {
        ChatRoom room = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND));

        return toChatRoomResponse(room, myId);
    }

    public List<ChatRoomResponseDto> getChatRooms(String myId){
        List<ChatRoom> chatRooms = chatRoomRepository.findBySellerIdOrReceiverIdOrderByModifiedTimeDesc(myId, myId);

        return chatRooms.stream()
                .map(room -> toChatRoomResponse(room, myId)
                ).collect(Collectors.toList());
    }


    public void deleteChatRoom(String myId, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(CHATROOM_NOT_FOUND));

        boolean isSeller = Objects.equals(chatRoom.getSellerId(), myId);
        boolean isReceiver = Objects.equals(chatRoom.getReceiverId(), myId);

        if (!isSeller && !isReceiver) {
            throw new CustomException(VALID_DELETE_CHATROOM);
        }

        if (isSeller) {
            chatRoom.setSellerId(null);
        } else {
            chatRoom.setReceiverId(null);
        }

        chatRoomRepository.save(chatRoom);

        if ((chatRoom.getSellerId() == null || chatRoom.getSellerId().isBlank()) &&
                (chatRoom.getReceiverId() == null || chatRoom.getReceiverId().isBlank())) {

            chatMessageRepository.deleteByRoomKey(chatRoom.getRoomKey());
            chatRoomRepository.delete(chatRoom);
        }
    }

    public void deleteChatRoomsByItemId(String myId, Long itemId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByItemId(itemId);

        List<String> roomIds = chatRooms.stream()
                .map(chatRoom -> String.valueOf(chatRoom.getId()))
                .collect(Collectors.toList());

        chatMessageRepository.deleteByRoomIdIn(roomIds);

        chatRoomRepository.deleteAll(chatRooms);
    }

    private ChatRoomResponseDto toChatRoomResponse(ChatRoom room, String myId) {
        String opponentId = room.getSellerId().equals(myId) ? room.getReceiverId() : room.getSellerId();

        User opponent = userRepository.findById(opponentId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Item item = null;
        String image = null;

        if (room.getItemId() != null) {
            item = itemRepository.findById(room.getItemId())
                    .orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));

            image = imageRepository.findTopByItemIdOrderByIdAsc(room.getItemId()).getFileUrl();
        }

        WantPost post = null;
        if (room.getPostId() != null) {
            post = wantPostRepository.findById(room.getPostId())
                    .orElseThrow(() -> new CustomException(WANT_POST_NOT_FOUND));
        }

        return ChatRoomResponseDto.from(room, opponent.getNickname(), item, image, post);
    }
}

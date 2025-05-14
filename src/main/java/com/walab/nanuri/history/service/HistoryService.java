package com.walab.nanuri.history.service;

import com.walab.nanuri.chat.entity.ChatRoom;
import com.walab.nanuri.chat.service.ChatParticipantService;
import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.commons.util.PostType;
import com.walab.nanuri.chat.repository.ChatRoomRepository;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.history.dto.response.ReceivedItemDto;
import com.walab.nanuri.history.dto.response.WaitingItemDto;
import com.walab.nanuri.image.repository.ImageRepository;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.history.dto.response.ApplicantDto;
import com.walab.nanuri.history.entity.History;
import com.walab.nanuri.history.repository.HistoryRepository;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.walab.nanuri.commons.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantService chatParticipantService;

    //Item 신청 (아이템 나눔 받고 싶다고 신청)
    @Transactional
    public void applicationItem(String receiverId, Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));

        //마감기한 지난 물건인지 확인
        validateItemNotExpired(item);

        //본인의 물건인지 확인
        if(receiverId.equals(item.getUserId())){
            throw new CustomException(VALID_ITEM);
        }

        //이미 나눔 신청한 물건인지 확인
        if(historyRepository.existsByItemIdAndReceivedId(itemId, receiverId)){
            throw new CustomException(DUPLICATE_APPLICATION_ITEM);
        }

        History history = History.toEntity(receiverId, itemId);
        historyRepository.save(history);

        String sellerId = item.getUserId();
        String roomKey = ChatRoom.createRoomKey(String.valueOf(item.getId()), sellerId, receiverId);

        ChatRoom chatRoom = chatRoomRepository.findByRoomKey(roomKey);

        if (chatRoom == null) {
            chatRoom = ChatRoom.builder()
                    .itemId(item.getId())
                    .historyId(history.getId())
                    .postType(PostType.ITEM)
                    .roomKey(roomKey)
                    .build();
            chatRoomRepository.save(chatRoom);

            chatParticipantService.enterRoom(chatRoom, receiverId);
            chatParticipantService.enterRoom(chatRoom, sellerId);

            item.setChatCount(item.getChatCount() + 1);
        }
    }

    //마감기한 지난 물건인지 확인 -> 마감기한 지난 물건시 신청 불가
    private void validateItemNotExpired(Item item) {
        if (item.getDeadline() != null && item.getDeadline().isBefore(LocalDateTime.now())) {
            item.setShareStatus(ShareStatus.EXPIRED);
            throw new CustomException(ITEM_DEADLINE_EXPIRED);
        }
    }

    //Item-신청자 리스트 조회
    public List<ApplicantDto> getAllApplicants(String sellerId, Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));
        if(!sellerId.equals(item.getUserId())){ //판매자가 아닐경우 -> 접근 권한 없음
            throw new CustomException(VALID_ITEM);
        }
        return historyRepository.findByItemId(itemId)
                .stream()
                .map(history -> {
                    User user = userRepository.findById(history.getReceivedId())
                            .orElseThrow(()-> new CustomException(USER_NOT_FOUND));
                    return ApplicantDto.from(history, user);
                })
                .toList();
    }

    //내가 대기 중인 Item 조회
    public List<WaitingItemDto> getAllWaitingItems(String receiverId){
        List<History> waitingList = historyRepository.findAllByReceivedIdAndIsConfirmedFalse(receiverId);

        return waitingList.stream()
                .map(history -> {
                    Item item = itemRepository.findById(history.getItemId()).
                            orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    return WaitingItemDto.from(item, history.getId(), image);
                })
                .toList();
    }

    //내가 받은 Item 조회
    public List<ReceivedItemDto> getAllReceivedItems(String receiverId){
        List<History> receivedList = historyRepository.findAllByReceivedIdAndIsConfirmedTrue(receiverId);

        return receivedList.stream()
                .map(history -> {
                    Item item = itemRepository.findById(history.getItemId()).
                            orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    return ReceivedItemDto.from(item, history.getId(), image);
                })
                .toList();
    }

    //Item 나눔 완료
    @Transactional
    public void completeItemApplication(String uniqueId, Long historyId){
        History history = historyRepository.findById(historyId).orElseThrow(() -> new CustomException(HISTORY_NOT_FOUND));
        Item item = itemRepository.findById(history.getItemId()).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));

        if(isNotOwner(uniqueId, item) || !uniqueId.equals(history.getReceivedId())){
            throw new CustomException(VALID_USER);
        }

        if (history.getIsConfirmed()) {
            history.markUnconfirmed();
            item.setShareStatus(ShareStatus.IN_PROGRESS);
        } else {
            history.markConfirmed();
            item.setShareStatus(ShareStatus.COMPLETED);
        }

        historyRepository.save(history);
        itemRepository.save(item);
    }

    //Item 나눔 신청 취소
    @Transactional
    public void cancelItemApplication(String receiverId, Long historyId){
        if(!historyRepository.existsByIdAndReceivedId(historyId, receiverId)){
            throw new CustomException(VALID_ITEM);
        }
        historyRepository.deleteById(historyId);
    }

    private boolean isNotOwner(String sellerId, Item item) {
        return !sellerId.equals(item.getUserId());
    }
}

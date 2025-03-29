package com.walab.nanuri.history.service;

import com.walab.nanuri.commons.exception.ItemNotExistException;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    //Item 신청 (아이템 나눔 받고 싶다고 신청)
    @Transactional
    public void applicationItem(String receiverId, Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotExistException::new);

        //본인의 물건인지 확인
        if(receiverId.equals(item.getUserId())){
            throw new RuntimeException("본인의 물건은 나눔 신청할 수 없습니다. ");
        }

        //이미 나눔 신청한 물건인지 확인
        if(historyRepository.existsByItemIdAndGetUserId(itemId, receiverId)){
            throw new RuntimeException("이미 나눔 신청한 물건입니다.");
        }

        History history = History.toEntity(receiverId, itemId);
        historyRepository.save(history);
    }

    //Item-신청자 리스트 조회
    @Transactional
    public List<ApplicantDto> getAllApplicants(String sellerId, Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotExistException::new);
        if(!sellerId.equals(item.getUserId())){ //판매자가 아닐경우 -> 접근 권한 없음
            throw new RuntimeException("물건 주인이 아니므로 접근 권한이 없습니다.");
        }
        return historyRepository.findById(itemId)
                .stream()
                .map(history -> {
                    User user = userRepository.findById(history.getGetUserId())
                            .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다. "));
                    return ApplicantDto.from(history, user);
                })
                .toList();
    }

    // 해당 유저 선택
    @Transactional
    public void selectReceiver(String sellerId, Long historyId){
        History history = historyRepository.findById(historyId).orElseThrow(ItemNotExistException::new);
        Item item = itemRepository.findById(history.getItemId()).orElseThrow(ItemNotExistException::new);

        if(!sellerId.equals(item.getUserId())){
            throw new RuntimeException("물건 주인이 아니므로 접근 권한이 없습니다.");
        }

        history.markSelected();
        historyRepository.save(history);
    }

    //Item 거래 완료
    @Transactional
    public void completeItemApplication(String sellerId, Long historyId){
        History history = historyRepository.findById(historyId).orElseThrow(ItemNotExistException::new);
        Item item = itemRepository.findById(history.getItemId()).orElseThrow(ItemNotExistException::new);
        if(!sellerId.equals(item.getUserId())){ //판매자가 아니라면 -> 접근 권한 없음
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        history.markConfirmed();
        historyRepository.save(history);
    }

    @Transactional
    public void cancelItemApplication(String receiverId, Long itemId){
        historyRepository.deleteByItemIdAndGetUserId(itemId, receiverId);
    }
}

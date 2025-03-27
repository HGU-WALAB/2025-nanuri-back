package com.walab.nanuri.itemHistory.service;

import com.walab.nanuri.commons.exception.ItemNotExistException;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.itemHistory.dto.request.ApplicantDto;
import com.walab.nanuri.itemHistory.entity.ItemHistory;
import com.walab.nanuri.itemHistory.repository.ItemHistoryRepository;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemHistoryService {
    private final ItemHistoryRepository itemHistoryRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    //Item 신청 (아이템 나눔을 신청)
    @Transactional
    public void applicationItem(String uniqueId, Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotExistException::new);

        //본인의 물건인지 확인
        if(uniqueId.equals(item.getUserId())){
            throw new RuntimeException("본인의 물건은 나눔 신청할 수 없습니다. ");
        }

        //이미 나눔 신청한 물건인지 확인
        if(itemHistoryRepository.existsByItemIdAndGetUserId(itemId, uniqueId)){
            throw new RuntimeException("이미 나눔 신청한 물건입니다.");
        }

        ItemHistory itemHistory = ItemHistory.builder()
                .itemId(itemId)
                .getUserId(uniqueId)
                .isFinished(false)
                .build();

        itemHistoryRepository.save(itemHistory);
    }


    //Item 나눔 신청 취소
    @Transactional
    public void cancelItemApplication(String uniqueId, Long itemId){
        itemHistoryRepository.deleteByItemIdAndGetUserId(itemId, uniqueId);
    }


    //Item-신청자 리스트 조회
    @Transactional
    public List<ApplicantDto> getAllApplicants(String uniqueId, Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotExistException::new);
        if(!uniqueId.equals(item.getUserId())){ //판매자가 아닐경우 -> 접근 권한 없음
            throw new RuntimeException("접근 권한이 없습니다.");
        }

        return itemHistoryRepository.findById(itemId)
                .stream()
                .map(history -> {
                    User user = userRepository.findById(history.getGetUserId())
                            .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다. "));
                    return new ApplicantDto(user.getUniqueId(), user.getNickname(), user.getRank());
                })
                .toList();
    }

    //Item 거래 완료
    @Transactional
    public void completeItemApplication(String uniqueId, Long itemId, String applicant){
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotExistException::new);
        if(!uniqueId.equals(item.getUserId())){ //판매자가 아니라면 -> 접근 권한 없음
            throw new RuntimeException("접근 권한이 없습니다.");
        }
        ItemHistory itemHistory = itemHistoryRepository.findByItemIdAndGetUserId(itemId, uniqueId)
                .orElseThrow(() -> new RuntimeException("해당 신청자가 존재하지 않습니다."));

        itemHistory = ItemHistory.builder()
                .id(itemHistory.getId())
                .itemId(itemHistory.getItemId())
                .getUserId(applicant)
                .isFinished(true)
                .build();

        itemHistoryRepository.save(itemHistory);
    }
}

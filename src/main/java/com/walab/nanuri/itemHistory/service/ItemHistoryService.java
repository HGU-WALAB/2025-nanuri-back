package com.walab.nanuri.itemHistory.service;

import com.walab.nanuri.itemHistory.entity.ItemHistory;
import com.walab.nanuri.itemHistory.repository.ItemHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemHistoryService {
    private final ItemHistoryRepository itemHistoryRepository;

    //Item 신청 (아이템 나눔을 신청)
    @Transactional
    public void applicateItem(String uniqueId, Long itemId){
        ItemHistory itemHistory = ItemHistory.builder()
                .itemId(itemId)
                .getUserId(uniqueId)
                .isFinished(false)
                .build();
        itemHistoryRepository.save(itemHistory);
    }


    //Item 나눔 신청 취소


    //Item-신청자 리스트 조회


    //Item 거래 완료

}

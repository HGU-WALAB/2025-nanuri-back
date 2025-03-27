package com.walab.nanuri.itemHistory.service;

import com.walab.nanuri.itemHistory.repository.ItemHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemHistoryService {
    private final ItemHistoryRepository itemHistoryRepository;

    //Item 거래 신청
    @Transactional
    public void applicateItem(Long itemId, String uniqueId){
        Item
        itemHistoryRepository.save(item);
    }


    //Item 거래 신청 취소


    //Item-신청자 리스트 조회


    //Item 거래 완료

}

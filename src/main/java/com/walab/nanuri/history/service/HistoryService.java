package com.walab.nanuri.history.service;

import com.walab.nanuri.commons.exception.ItemNotExistException;
import com.walab.nanuri.commons.util.Time;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.history.dto.request.ApplicantDto;
import com.walab.nanuri.history.entity.History;
import com.walab.nanuri.history.repository.HistoryRepository;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;
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
        if(historyRepository.existsByItemIdAndGetUserId(itemId, uniqueId)){
            throw new RuntimeException("이미 나눔 신청한 물건입니다.");
        }

        History history = History.builder()
                .itemId(itemId)
                .getUserId(uniqueId)
                .isFinished(false)
                .build();

        historyRepository.save(history);
    }


    //Item 나눔 신청 취소
    @Transactional
    public void cancelItemApplication(String uniqueId, Long itemId){
        historyRepository.deleteByItemIdAndGetUserId(itemId, uniqueId);
    }


    //Item-신청자 리스트 조회
    @Transactional
    public List<ApplicantDto> getAllApplicants(String uniqueId, Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotExistException::new);
        if(!uniqueId.equals(item.getUserId())){ //판매자가 아닐경우 -> 접근 권한 없음
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

    //Item 거래 완료
    @Transactional
    public void completeItemApplication(String uniqueId, Long itemId, String applicant){
        Item item = itemRepository.findById(itemId).orElseThrow(ItemNotExistException::new);
        if(!uniqueId.equals(item.getUserId())){ //판매자가 아니라면 -> 접근 권한 없음
            throw new RuntimeException("접근 권한이 없습니다.");
        }
        History history = historyRepository.findByItemIdAndGetUserId(itemId, uniqueId)
                .orElseThrow(() -> new RuntimeException("해당 신청자가 존재하지 않습니다."));

        history = History.builder()
                .id(history.getId())
                .itemId(history.getItemId())
                .getUserId(applicant)
                .isFinished(true)
                .build();

        historyRepository.save(history);
    }
}

package com.walab.nanuri.item.service;

import com.walab.nanuri.commons.exception.ItemAccessDeniedException;
import com.walab.nanuri.commons.exception.ItemNotExistException;
import com.walab.nanuri.commons.util.Time;
import com.walab.nanuri.item.dto.request.ItemRequestDto;
import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.item.dto.response.ItemResponseDto;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    //Item 추가
    @Transactional
    public void createItem(String uniqueId, ItemRequestDto itemDto) {
        Item item = Item.builder()
                .title(itemDto.getTitle())
                .description(itemDto.getDescription())
                .place(itemDto.getPlace())
                .viewCount(0)
                .category(itemDto.getCategory())
                .userId(uniqueId)
                .isFinished(false)
                .build();

        itemRepository.save(item);
    }

    //Item 전체 조회(일반 전체 조회, 카테고리 선택 후 전체 조회)
    public List<ItemListResponseDto> getAllItems(String category) {
        List<Item> items;
        if (category.isEmpty()) {
            items = itemRepository.findAll();
        } else {
            items = itemRepository.findAllByCategory(category);
        }

        return items.stream()
                .map(ItemListResponseDto::from)
                .toList();
    }


    //나눔 중인 나의 Item 조회

    //Item 단건 조회
    public ItemResponseDto getItemById(String uniqueId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(RuntimeException::new);

        if (item.getUserId().equals(uniqueId)) { //자신의 학번과 물건의 판매자 아이디가 같음 -> 판매자임
            return ItemResponseDto.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .viewCount(item.getViewCount())
                    .category(item.getCategory())
                    .isFinished(item.getIsFinished())
                    .createdTime(Time.calculateTime(Timestamp.valueOf(item.getCreatedTime())))
                    .wishCount(item.getWishCount())
                    .isOwner(true)
                    .build();
        } else { //구매자라면
            return ItemResponseDto.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .viewCount(item.getViewCount())
                    .category(item.getCategory())
                    .isFinished(item.getIsFinished())
                    .createdTime(Time.calculateTime(Timestamp.valueOf(item.getCreatedTime())))
                    .wishCount(item.getWishCount())
                    .isOwner(false)
                    .build();
        }
    }


    //Item 수정
    @Transactional
    public void updateItem(String uniqueId, Long updateId, ItemRequestDto itemDto) {
        Item findItem = itemRepository.findById(updateId).orElseThrow(ItemNotExistException::new);

        if(findItem.getUserId().equals(uniqueId)) { // 아이템 주인이 맞을 경우
            findItem.update(itemDto.getTitle(), itemDto.getDescription(), itemDto.getPlace(), itemDto.getCategory());
        } else {
            throw new ItemAccessDeniedException("아이템에 대한 권한이 없는 사용자입니다.");
        }
    }


    //Item 삭제하기
    @Transactional
    public void deleteItem(String uniqueId, Long itemId) {
        Item findItem = itemRepository.findById(itemId).orElseThrow(ItemNotExistException::new);
        if(findItem.getUserId().equals(uniqueId)) { // 아이템 주인이 맞을 경우
            itemRepository.delete(findItem);
        } else {
            throw new ItemAccessDeniedException("아이템에 대한 권한이 없는 사용자입니다.");
        }
    }

}

package com.walab.nanuri.item.service;

import com.walab.nanuri.item.dto.request.ItemRequestDto;
import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.item.dto.response.ItemResponseDto;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    //Item 추가
    @Transactional
    public void createItem(String uniqueId, ItemRequestDto itemDto){
//        itemRepository.save(itemDto.toEntity());
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

    //Item 전체 조회
    @Transactional
    public List<ItemListResponseDto> getAllItems(){
        List<Item> items = itemRepository.findAll();
        List<ItemListResponseDto> itemDtoList = new ArrayList<>();

        for(Item item : items){
            ItemListResponseDto itemDto = ItemListResponseDto.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .createdTime(item.getCreatedTime())
                    .build();
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }


    //나눔 중인 나의 Item 조회
    @Transactional
    public List<ItemListResponseDto> getMySharingItem(){
        List<Item> items =
    }

    //Item 단건 조회
    @Transactional
    public ItemResponseDto getItemById(String uniqueId, Long itemId){
        Item item = itemRepository.findById(itemId).orElseThrow(RuntimeException::new);

        if(item.getUserId().equals(uniqueId)){ //자신의 학번과 물건의 판매자 아이디가 같음 -> 판매자임
            return ItemResponseDto.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .viewCount(item.getViewCount())
                    .category(item.getCategory())
                    .isFinished(item.getIsFinished())
                    .createdTime(item.getCreatedTime())
                    .wishCount(item.getWishCount())
                    .isOwner(true)
                    .build();
        }
        else{ //구매자라면
            return ItemResponseDto.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .viewCount(item.getViewCount())
                    .category(item.getCategory())
                    .isFinished(item.getIsFinished())
                    .createdTime(item.getCreatedTime())
                    .wishCount(item.getWishCount())
                    .isOwner(false)
                    .build();
        }
    }


    //Item 수정
    @Transactional
    public boolean updateItem(Long updateId, ItemRequestDto itemDto) {
        Item findItem = itemRepository.findById(updateId).orElseThrow(()->new IllegalArgumentException("게시글을 찾을 수 없습니다"));
        findItem.update(itemDto.getTitle(), itemDto.getDescription(), itemDto.getPlace(), itemDto.getCategory());
        try{
            itemRepository.save(findItem);
        } catch (Exception e){
            return false;
        }
        return true;
    }


    //Item 삭제하기(Delete)
    @Transactional
    public boolean deleteItem(Long id){
        itemRepository.deleteById(id);
        return true;
    }

}

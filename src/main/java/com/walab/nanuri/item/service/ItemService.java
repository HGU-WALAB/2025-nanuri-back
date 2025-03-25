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
                .category(itemDto.getCategory())
                .userId(uniqueId)
                .isFinished(false)
                .build();

        itemRepository.save(item);
    }

    //전체 Item 가져오기
    @Transactional
    public List<ItemListResponseDto> getAllItems(){
        List<Item> items = itemRepository.findAll();
        List<ItemListResponseDto> itemDtoList = new ArrayList<>();

        for(Item item : items){
            ItemListResponseDto itemDto = ItemListResponseDto.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .viewCount(item.getViewCount())
                    .category(item.getCategory())
                    .isFinished(item.getIsFinished())
                    .createdTime(item.getCreatedTime())
                    .build();
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }


    //Item 하나 가져오기
    @Transactional
    public ItemResponseDto getItemById(Long itemId, String uniqueId){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(RuntimeException::new);

        if(item.getUserId().equals(uniqueId)){ //판매자라면
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


    //Item 수정하기(Update)
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

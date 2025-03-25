package com.walab.nanuri.item.service;

import com.walab.nanuri.item.dto.request.ItemRequestDto;
import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.item.dto.response.ItemResponseDto;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
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
    public void createItem(String userId, ItemRequestDto itemDto){
//        itemRepository.save(itemDto.toEntity());
        Item item = Item.builder()
                .title(itemDto.getTitle())
                .description(itemDto.getDescription())
                .place(itemDto.getPlace())
                .viewCount(0)
                .category("test")
                .userId(Long.parseLong(userId))
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


    //Item 하나 가져오기(판매자 관점)
    @Transactional
    public ItemResponseDto getItemBySeller(Long itemId){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(RuntimeException::new);

        return ItemResponseDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .viewCount(item.getViewCount())
                .category(item.getCategory())
                .isFinished(item.getIsFinished())
                .createdTime(item.getCreatedTime())
                .wishCount(item.get)
                .isOwner()
                .build();
    }

    //Item 하나 가져오기(판매자 아닌 관점 -> 구매자 관점)
    @Transactional
    public ItemListResponseDto getItemByBuyer(Long id){
        Item item = itemRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        return ItemListResponseDto.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .place(item.getPlace())
                .viewCount(item.getViewCount())
                .category(item.getCategory())
                .userId(item.getUserId())
                .isFinished(item.getIsFinished())
                .postTime(item.getCreatedDate())
                .build();
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

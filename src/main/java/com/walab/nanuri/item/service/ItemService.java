package com.walab.nanuri.item.service;

import com.walab.nanuri.item.dto.ItemRequestDto;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    //Item 추가
    @Transactional
    public void createItem(ItemRequestDto itemDto){
        itemRepository.save(itemDto.ToEntity());
    }

    //전체 Item 가져오기
    @Transactional
    public List<ItemRequestDto> getAllItems(){
        List<Item> items = itemRepository.findAll();
        List<ItemRequestDto> itemDtoList = new ArrayList<>();

        for(Item item : items){
            ItemRequestDto itemDto = ItemRequestDto.builder()
                    .id(item.getId())
                    .title(item.getTitle())
                    .description(item.getDescription())
                    .place(item.getPlace())
                    .viewCount(item.getViewCount())
                    .category(item.getCategory())
                    .userId(item.getUserId())
                    .build();
            itemDtoList.add(itemDto);
        }
        return itemDtoList;
    }

    //Item 하나 가져오기(View)
    @Transactional
    public ItemRequestDto getItemById(Long id){
        Optional<Item> itemWrapper = itemRepository.findById(id);
        Item item = itemWrapper.get();

        return ItemRequestDto.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .place(item.getPlace())
                .viewCount(item.getViewCount())
                .category(item.getCategory())
                .userId(item.getUserId())
                .build();
    }

    //Item 수정하기(Update)
    @Transactional
    public void updateItem(Long id, ItemRequestDto itemDto) {
        Optional<Item> findItem = itemRepository.findById(id);
        Item item = findItem.get();



    }

    //Item 삭제하기(Delete)
    @Transactional
    public void deleteItem(Long id){
        itemRepository.deleteById(id);
    }




}

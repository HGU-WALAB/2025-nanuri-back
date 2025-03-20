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

    @Transactional
    public void saveItem(ItemRequestDto itemDto){
        itemRepository.save(itemDto.ToEntity());
    }

    public void createItem(ItemRequestDto itemDto){

    }

    //전체 Item 가져오기
    @Transactional
    public List<ItemRequestDto> getAllItems(){
        List<Item> items = itemRepository.findAll();
        List<ItemRequestDto> itemDtoList = new ArrayList<>();

        for(Item item : items){
            ItemRequestDto itemDto = ItemRequestDto.builder()
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


}

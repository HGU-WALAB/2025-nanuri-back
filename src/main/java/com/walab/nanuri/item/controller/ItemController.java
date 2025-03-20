package com.walab.nanuri.item.controller;

import com.walab.nanuri.item.dto.ItemRequestDto;
import com.walab.nanuri.item.dto.ItemResponseDto;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    @PostMapping("/api/item")
    public ItemResponseDto createItem(@RequestBody ItemRequestDto request){
        itemService.createItem(request);

        return new ItemResponseDto(
                request.ToEntity().getTitle(),
                request.ToEntity().getDescription(),
                request.ToEntity().getPlace(),
                request.ToEntity().getViewCount(),
                request.ToEntity().getCategory(),
                request.ToEntity().getUserId(),
                request.ToEntity().getIsFinished(),
                request.ToEntity().getCreatedAt()
        );
    }

    //아이템 전체 조회
    @GetMapping("/api/items")
    public ItemResponseDto getAllItems(){
        return itemService.getAllItems();
    }


    //아이템 단건 조회
    @GetMapping("/api/item/{itemId}")
    public ItemResponseDto getItem(@PathVariable Long itemId){
        ItemRequestDto item = itemService.getItemById(itemId);

        return new ItemResponseDto(
                item.ToEntity().getTitle(),
                item.ToEntity().getDescription(),
                item.ToEntity().getPlace(),
                item.ToEntity().getViewCount(),
                item.ToEntity().getCategory(),
                item.ToEntity().getUserId(),
                item.ToEntity().getIsFinished(),
                item.ToEntity().getCreatedAt()
        );
    }
}

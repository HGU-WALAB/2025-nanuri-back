package com.walab.nanuri.item.controller;

import com.walab.nanuri.item.dto.ItemRequestDto;
import com.walab.nanuri.item.dto.ItemResponseDto;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

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

    //Item 전체 조회
    @GetMapping("/api/items")
    public List<ItemRequestDto> getAllItems(){
        return itemService.getAllItems();
    }


    //Item 단건 조회
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

    //Item 수정
    @PatchMapping("/api/item/{itemId}")
    public ItemResponseDto updateItem(@PathVariable Long itemId, @RequestBody ItemRequestDto request){
        itemService.updateItem(itemId, request);
        Optional<Item> findItem = itemRepository.findById(itemId);
        Item item = findItem.get();

        return new ItemResponseDto(
                item.getTitle(),
                item.getDescription(),
                item.getPlace(),
                item.getViewCount(),
                item.getCategory(),
                item.getUserId(),
                item.getIsFinished(),
                item.getCreatedAt()
        );
    }


    //Item 삭제
    @DeleteMapping("/api/item/{itemId}")
    public void delteItem(@PathVariable Long itemId){
        itemService.deleteItem(itemId);
    }
}

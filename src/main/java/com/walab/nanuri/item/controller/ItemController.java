package com.walab.nanuri.item.controller;

import com.walab.nanuri.item.dto.ItemRequestDto;
import com.walab.nanuri.item.dto.ItemResponseDto;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

    //Item 추가
    @PostMapping("/api/item")
    public ResponseEntity<Boolean> createItem(@RequestBody ItemRequestDto request){
        try{
            itemService.createItem(request);
        }catch (Exception e){
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }

    //Item 전체 조회
    @GetMapping("/api/items")
    public List<ItemResponseDto> getAllItems(){
        return itemService.getAllItems();
    }


    //Item 단건 조회
    @GetMapping("/api/item/{itemId}")
    public ResponseEntity<ItemResponseDto> getItem(@PathVariable Long itemId){
        return ResponseEntity.ok(itemService.getItemById(itemId));
    }

    //Item 수정
    @PatchMapping("/api/item/{itemId}")
    public ResponseEntity<Boolean> updateItem(@PathVariable Long itemId, @RequestBody ItemRequestDto request){
        try{
            itemService.updateItem(itemId, request);
        } catch (Exception e){
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }


    //Item 삭제
    @DeleteMapping("/api/item/{itemId}")
    public ResponseEntity<Boolean> deleteItem(@PathVariable Long itemId){
        return ResponseEntity.ok(itemService.deleteItem(itemId));
    }
}

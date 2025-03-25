package com.walab.nanuri.item.controller;

import com.walab.nanuri.item.dto.request.ItemRequestDto;
import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    //Item 추가
    @PostMapping("/api/item")
    public ResponseEntity<Boolean> createItem(@RequestBody ItemRequestDto request, @AuthenticationPrincipal String uniqueId){
        try{
            itemService.createItem(uniqueId, request);
        }catch (Exception e){
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);
    }

    //Item 전체 조회
    @GetMapping("/api/items")
    public List<ItemListResponseDto> getAllItems(){
        return itemService.getAllItems();
    }


    //Item 단건 조회
    @GetMapping("/api/item/{itemId}")
    public ResponseEntity<ItemListResponseDto> getItem(@PathVariable Long itemId){
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

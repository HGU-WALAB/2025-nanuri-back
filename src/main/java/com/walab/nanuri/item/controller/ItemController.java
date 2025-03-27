package com.walab.nanuri.item.controller;

import com.walab.nanuri.item.dto.request.ItemRequestDto;
import com.walab.nanuri.item.dto.response.ItemCreateResponseDto;
import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.item.dto.response.ItemResponseDto;
import com.walab.nanuri.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ItemController {

    private final ItemService itemService;

    //Item 추가
    @PostMapping()
    public ResponseEntity<ItemCreateResponseDto> createItem(@AuthenticationPrincipal String uniqueId,
                                              @RequestBody ItemRequestDto request) {
        Long itemId = itemService.createItem(uniqueId, request);
        ItemCreateResponseDto responseDto = ItemCreateResponseDto.builder()
                .itemId(itemId)
                .build();
        return ResponseEntity.ok().body(responseDto);
    }


    //Item 전체 조회
    @GetMapping()
    public List<ItemListResponseDto> getAllItems(@RequestParam(required = false, defaultValue = "") String category) {
        return itemService.getAllItems(category);
    }


    //Item 단건 조회
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@AuthenticationPrincipal String uniqueId,
                                                       @PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(uniqueId, itemId));
    }


    //Item 수정
    @PatchMapping("/{itemId}")
    public ResponseEntity<Void> updateItem(@AuthenticationPrincipal String uniqueId, @PathVariable Long itemId, @RequestBody ItemRequestDto request) {
        itemService.updateItem(uniqueId, itemId, request);
        return ResponseEntity.ok().body(null);
    }


    //Item 삭제
    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@AuthenticationPrincipal String uniqueId, @PathVariable Long itemId) {
        itemService.deleteItem(uniqueId, itemId);
        return ResponseEntity.ok().body(null);
    }
}

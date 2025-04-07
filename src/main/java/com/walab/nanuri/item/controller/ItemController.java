package com.walab.nanuri.item.controller;

import com.walab.nanuri.commons.entity.ShareStatus;
import com.walab.nanuri.item.dto.request.ItemRequestDto;
import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.item.dto.response.ItemResponseDto;
import com.walab.nanuri.item.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
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
    @PostMapping("/item")
    public ResponseEntity<Long> createItem(@AuthenticationPrincipal String uniqueId,
                                           @RequestBody ItemRequestDto request) {
        return ResponseEntity.ok().body(itemService.createItem(uniqueId, request));
    }


    //Item 전체 조회
    @GetMapping("/items")
    public List<ItemListResponseDto> getAllItems(@RequestParam(required = false, defaultValue = "") String category) {
        return itemService.getAllItems(category);
    }

    @GetMapping("/items/{userNickname}")
    public List<ItemListResponseDto> getUserItems(@PathVariable String userNickname) {
        return itemService.getItemsByUserId(userNickname);
    }


    //Item 단건 조회
    @GetMapping("/item/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@AuthenticationPrincipal String uniqueId,
                                                       @PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(uniqueId, itemId));
    }

    @GetMapping("/items/shared")
    public ResponseEntity<List<ItemListResponseDto>> getOngoingMyItems(@AuthenticationPrincipal String uniqueId,
                                                                       @RequestParam String done) {
        return ResponseEntity.ok(itemService.getOngoingMyItems(uniqueId, done));
    }

    //Item 수정
    @PatchMapping("/item/{itemId}")
    public ResponseEntity<Void> updateItem(@AuthenticationPrincipal String uniqueId, @PathVariable Long itemId, @RequestBody ItemRequestDto request) {
        itemService.updateItem(uniqueId, itemId, request);
        return ResponseEntity.ok().body(null);
    }


    //Item 삭제
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Void> deleteItem(@AuthenticationPrincipal String uniqueId, @PathVariable Long itemId) {
        itemService.deleteItem(uniqueId, itemId);
        return ResponseEntity.ok().body(null);
    }
}

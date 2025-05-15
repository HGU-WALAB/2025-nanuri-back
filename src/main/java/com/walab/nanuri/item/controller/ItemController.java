package com.walab.nanuri.item.controller;

import com.walab.nanuri.item.dto.request.ItemRequestDto;
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
    @PostMapping("/item")
    public ResponseEntity<Long> createItem(@AuthenticationPrincipal String uniqueId,
                                           @RequestBody ItemRequestDto request) {
        return ResponseEntity.ok().body(itemService.createItem(uniqueId, request));
    }


    //Item 전체 조회
    @GetMapping("/items")
    public List<ItemListResponseDto> getAllItems(@AuthenticationPrincipal String uniqueId,
                                                 @RequestParam(required = false, defaultValue = "") String category) {
        return itemService.getAllItems(uniqueId, category);
    }

    //다른 User의 Item 전체 조회
    @GetMapping("/items/{userNickname}")
    public List<ItemListResponseDto> getUserItems(@PathVariable String userNickname) {
        return itemService.getItemsByUserNickname(userNickname);
    }

    //Item 단건 조회
    @GetMapping("/item/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@AuthenticationPrincipal String uniqueId,
                                                       @PathVariable Long itemId) {
        return ResponseEntity.ok(itemService.getItemById(uniqueId, itemId));
    }

    //나눔 중 혹은 완료된 나의 Item 조회
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

    //제목으로 아이템 검색
    @GetMapping("/items/search/title/{title}")
    public ResponseEntity<List<ItemListResponseDto>> getSearchTitleItems(@AuthenticationPrincipal String uniqueId,
                                                                         @PathVariable String title,
                                                                         @RequestParam(required = false, defaultValue = "") String category) {
        return ResponseEntity.ok(itemService.getSearchTitleItems(uniqueId, title, category));
    }

    //내용으로 아이템 검색
    @GetMapping("/items/search/description/{description}")
    public ResponseEntity<List<ItemListResponseDto>> getSearchDescriptionItems(@AuthenticationPrincipal String uniqueId,
                                                                                @PathVariable String description,
                                                                                @RequestParam(required = false, defaultValue = "") String category) {
        return ResponseEntity.ok(itemService.getSearchDescriptionItems(uniqueId, description, category));
    }

    //제목과 내용으로 아이템 검색
    @GetMapping("/items/search/{title}/{description}")
    public ResponseEntity<List<ItemListResponseDto>> getSearchTitleAndDescriptionItems(@AuthenticationPrincipal String uniqueId,
                                                                                         @PathVariable String title,
                                                                                         @PathVariable String description,
                                                                                         @RequestParam(required = false, defaultValue = "") String category) {
        return ResponseEntity.ok(itemService.getSearchTitleAndDescriptionItems(uniqueId, title, description, category));
    }

    //닉네임으로 아이템 검색
    @GetMapping("/items/search/nickname/{nickname}")
    public ResponseEntity<List<ItemListResponseDto>> getSearchNicknameItems(@AuthenticationPrincipal String uniqueId,
                                                                             @PathVariable String nickname,
                                                                             @RequestParam(required = false, defaultValue = "") String category) {
        return ResponseEntity.ok(itemService.getSearchNicknameItems(uniqueId, nickname, category));
    }

    //내일 나눔 마감인 아이템 조회
    @GetMapping("/items/deadline")
    public ResponseEntity<List<ItemListResponseDto>> getDeadlineItems(@AuthenticationPrincipal String uniqueId) {
        return ResponseEntity.ok(itemService.getDeadlineItems(uniqueId));
    }





}

package com.walab.nanuri.itemHistory.controller;

import com.walab.nanuri.itemHistory.dto.request.ItemIdRequestDto;
import com.walab.nanuri.itemHistory.entity.ItemHistory;
import com.walab.nanuri.itemHistory.service.ItemHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemHistoryController {
    private final ItemHistoryService itemHistoryService;

    //Item 신청 (아이템 나눔을 신청)
    @PostMapping("/api/history")
    public ResponseEntity<String> applicantItem(@AuthenticationPrincipal String uniqueId,
                                                @RequestBody ItemIdRequestDto request) {
        itemHistoryService.applicateItem(uniqueId, request.getItemId());
        return ResponseEntity.ok("신청 완료!");
    }

    //Item 나눔 신청 취소
}

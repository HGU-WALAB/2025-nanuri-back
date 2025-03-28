package com.walab.nanuri.history.controller;

import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.history.dto.request.ApplicantDto;
import com.walab.nanuri.history.dto.request.ItemIdRequestDto;
import com.walab.nanuri.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;
    private final ItemRepository itemRepository;

    //Item 신청 (아이템 나눔을 신청)
    @PostMapping("/api/history")
    public ResponseEntity<String> applicationItem(@AuthenticationPrincipal String uniqueId,
                                                @RequestBody ItemIdRequestDto request) {
        historyService.applicationItem(uniqueId, request.getItemId());
        return ResponseEntity.ok().body(null);
    }

    //Item 나눔 신청 취소
    @DeleteMapping("/api/history")
    public ResponseEntity<String> cancelItemApplication(@AuthenticationPrincipal String uniqueId,
                                                        @RequestBody ItemIdRequestDto request) {
        historyService.cancelItemApplication(uniqueId, request.getItemId());
        return ResponseEntity.ok().body(null);
    }

    //Item-신청자 리스트 조회
    @GetMapping("/api/history/{itemId}")
    public List<ApplicantDto> getAllApplicants(@AuthenticationPrincipal String uniqueId,
                                               @PathVariable long itemId) {
        return historyService.getAllApplicants(uniqueId, itemId);
    }

    //Item 거래 완료
    @PatchMapping("/api/history/{itemId}/{applicant}")
    public ResponseEntity<String> completeItemApplication(@AuthenticationPrincipal String uniqueId, @PathVariable Long itemId, @PathVariable String applicant){
        historyService.completeItemApplication(uniqueId, itemId, applicant);
        return ResponseEntity.ok().body(null);
    }
}

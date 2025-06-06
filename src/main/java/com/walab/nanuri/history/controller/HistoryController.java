package com.walab.nanuri.history.controller;

import com.walab.nanuri.history.dto.response.ApplicantDto;
import com.walab.nanuri.history.dto.request.ItemIdRequestDto;
import com.walab.nanuri.history.dto.response.ReceivedItemDto;
import com.walab.nanuri.history.dto.response.WaitingItemDto;
import com.walab.nanuri.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryService historyService;

    //Item 신청 (아이템 나눔을 신청)
    @PostMapping("")
    public ResponseEntity<String> applicationItem(@AuthenticationPrincipal String uniqueId,
                                                @RequestBody ItemIdRequestDto request) {
        historyService.applicationItem(uniqueId, request.getItemId());
        return ResponseEntity.ok().body(null);
    }

    //Item-신청자 리스트 조회
    @GetMapping("/{itemId}")
    public List<ApplicantDto> getAllApplicants(@AuthenticationPrincipal String uniqueId,
                                               @PathVariable Long itemId) {
        return historyService.getAllApplicants(uniqueId, itemId);
    }

    //Item 거래 완료
    @PatchMapping("/{historyId}/complete")
    public ResponseEntity<String> completeItemApplication(@AuthenticationPrincipal String uniqueId,
                                                          @PathVariable Long historyId
    ){
        historyService.completeItemApplication(uniqueId, historyId);
        return ResponseEntity.ok().body(null);
    }

    //Item 나눔 신청 취소
    @DeleteMapping("/{historyId}")
    public ResponseEntity<String> cancelItemApplication(@AuthenticationPrincipal String uniqueId,
                                                        @PathVariable Long historyId) {
        historyService.cancelItemApplication(uniqueId, historyId);
        return ResponseEntity.ok().body(null);
    }

    //내가 대기 중인 Item 조회
    @GetMapping("/receiving")
    public List<WaitingItemDto> getAllWaitingItems(@AuthenticationPrincipal String uniqueId) {
        return historyService.getAllWaitingItems(uniqueId);
    }


    //내가 받은 나눔 Item 조회
    @GetMapping("/receive-done")
    public List<ReceivedItemDto> getReceivedItems(@AuthenticationPrincipal String uniqueId) {
        return historyService.getAllReceivedItems(uniqueId);
    }
}

package com.walab.nanuri.notification.controller;

import com.walab.nanuri.notification.dto.request.NotificationRequestDto;
import com.walab.nanuri.notification.dto.response.NotificationResponseDto;
import com.walab.nanuri.notification.entity.Notification;
import com.walab.nanuri.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    // 알림 전송 및 알림 저장
    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody NotificationRequestDto notificationRequestDto) {
        notificationService.sendNotification(notificationRequestDto);
        return ResponseEntity.ok().build();
    }

    //알림 리스트 조회
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getMyNotificationList(@AuthenticationPrincipal String uniqueId) {
        return ResponseEntity.ok(notificationService.getMyAlarmList(uniqueId));
    }

    //알림 읽음 처리
    @PatchMapping("/{notificationId}")
    public ResponseEntity<Void> markNotification(@PathVariable Long notificationId) {
        notificationService.markNotification(notificationId);
        return ResponseEntity.ok().build();
    }
}

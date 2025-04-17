package com.walab.nanuri.alarm.controller;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.walab.nanuri.alarm.dto.request.FcmTokenRequestDto;
import com.walab.nanuri.alarm.dto.response.FcmTokenResponseDto;
import com.walab.nanuri.alarm.service.FcmService;
import com.walab.nanuri.alarm.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {
    private final NotificationService notificationService;
    private final FcmService fcmService;

    //user DB에 토큰 저장
    @PostMapping("/token")
    public ResponseEntity<FcmTokenResponseDto> saveFcmToken(@AuthenticationPrincipal String uniqueId,
                                                            @RequestBody FcmTokenRequestDto fcmTokenRequestDto) {
        FcmTokenResponseDto response = fcmService.saveFcmToken(uniqueId, fcmTokenRequestDto.getToken());
        return ResponseEntity.ok(response);
    }

    //토큰 삭제
    @DeleteMapping("/token")
    public ResponseEntity<Void> deleteFcmToken(@AuthenticationPrincipal String uniqueId) {
        fcmService.deleteFcmToken(uniqueId);
        return ResponseEntity.ok().build();
    }

}

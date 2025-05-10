package com.walab.nanuri.notification.controller;

import com.walab.nanuri.notification.dto.request.FcmTokenRequestDto;
import com.walab.nanuri.notification.dto.response.FcmTokenResponseDto;
import com.walab.nanuri.notification.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm")
public class FcmController {
    private final FcmService fcmService;

    //FCM 토큰 저장
    @PostMapping("/token")
    public ResponseEntity<FcmTokenResponseDto> saveFcmToken(@AuthenticationPrincipal String uniqueId,
                                                            @RequestBody FcmTokenRequestDto fcmTokenRequestDto) {
        return ResponseEntity.ok(fcmService.saveFcmToken(uniqueId, fcmTokenRequestDto.getToken()));
    }
}

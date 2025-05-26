package com.walab.nanuri.notification.service;

import com.walab.nanuri.notification.dto.request.NotificationRequestDto;
import com.walab.nanuri.notification.dto.response.NotificationResponseDto;
import com.walab.nanuri.notification.entity.FcmToken;
import com.walab.nanuri.notification.entity.Notification;
import com.walab.nanuri.notification.repository.FcmTokenRepository;
import com.walab.nanuri.notification.repository.NotificationRepository;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.commons.exception.ErrorCode;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.walab.nanuri.commons.exception.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // 알림 전송 및 알림 저장
    public void sendNotification(NotificationRequestDto notificationRequestDto) {
        //알림 받을 유저 찾기
        User user = userRepository.findById(notificationRequestDto.getReceiverId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //user의 fcmToken들 가져오기
        List<String> fcmTokens = fcmService.getFcmTokens(user.getUniqueId());
        if (fcmTokens.isEmpty()) throw new CustomException(ErrorCode.FCM_TOKEN_NOT_FOUND);

        // 해당 유저의 모든 디바이스에 FCM 메시지 전송
        for(String token: fcmTokens) {
            fcmService.sendMessageTo(token, notificationRequestDto.getTitle(), notificationRequestDto.getBody());

            Notification notification = Notification.builder()
                    .receiver(user)
                    .title(notificationRequestDto.getTitle())
                    .body(notificationRequestDto.getBody())
                    .itemId(notificationRequestDto.getItemId())
                    .isRead(false)
                    .build();

            notificationRepository.save(notification);
        }
    }


    // 알림 리스트 조회
    public List<NotificationResponseDto> getMyAlarmList(String uniqueId) {
        return notificationRepository.findByReceiver_UniqueIdOrderByCreatedTimeDesc(uniqueId)
                .stream()
                .map(NotificationResponseDto::from)
                .toList();
    }


    // 알림 읽음 처리
    public void markNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}

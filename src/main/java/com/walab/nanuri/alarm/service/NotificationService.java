package com.walab.nanuri.alarm.service;

import com.walab.nanuri.alarm.dto.request.NotificationRequestDto;
import com.walab.nanuri.alarm.entity.Notification;
import com.walab.nanuri.alarm.repository.NotificationRepository;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.commons.exception.ErrorCode;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.walab.nanuri.commons.exception.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final FcmService fcmService;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    // User에게 알림 전송 및 알림 저장
    public void sendNotification(NotificationRequestDto notificationRequestDto) {
        User user = userRepository.findById(notificationRequestDto.getReceiverId()) //알림 받을 유저 찾기
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        String token = user.getFcmToken();
        if (token == null || token.isBlank()) throw new CustomException(ErrorCode.FCM_TOKEN_NOT_FOUND);

        fcmService.sendMessageTo(token, notificationRequestDto.getTitle(), notificationRequestDto.getBody());

        Notification notification = Notification.builder()
                .receiverId(notificationRequestDto.getReceiverId())
                .title(notificationRequestDto.getTitle())
                .body(notificationRequestDto.getBody())
                .itemId(notificationRequestDto.getItemId())
                .isRead(false)
                .fcmToken(token)
                .build();

        notificationRepository.save(notification);
    }


    // 알림 리스트 조회
    public List<Notification> getMyAlarmList(String uniqueId) {
        return notificationRepository.findByReceiverIdOrderByCreatedTimeDesc(uniqueId);
    }


    // 알림 읽음 처리
    public void markNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notification.setRead(true);
        notificationRepository.save(notification);
    }
}

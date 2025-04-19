package com.walab.nanuri.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.walab.nanuri.notification.dto.response.FcmTokenResponseDto;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.walab.nanuri.commons.exception.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {
    private final UserRepository userRepository;

    //user DB에 토큰 저장
    public FcmTokenResponseDto saveFcmToken(String uniqueId, String token) {
        User user = userRepository.findById(uniqueId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        user.updateFcmToken(token);
        userRepository.save(user);
        return new FcmTokenResponseDto(token);
    }

    //토큰 삭제
    public void deleteFcmToken(String uniqueId) {
        User user = userRepository.findById(uniqueId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        user.updateFcmToken(null);
        userRepository.save(user);
    }

    // 메시지 전송 (server -> firebase)
    public void sendMessageTo(String token, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(token)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("FCM message sent successfully: " + response);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("Failed to send FCM message", e);
        }
    }


}

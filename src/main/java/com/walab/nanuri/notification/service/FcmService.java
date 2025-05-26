package com.walab.nanuri.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.walab.nanuri.commons.exception.ErrorCode;
import com.walab.nanuri.notification.dto.response.FcmTokenResponseDto;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.notification.entity.FcmToken;
import com.walab.nanuri.notification.repository.FcmTokenRepository;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import static com.walab.nanuri.commons.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FcmService {
    private final UserRepository userRepository;
    private final FcmTokenRepository fcmTokenRepository;

    // FCM 토큰 저장
    public FcmTokenResponseDto saveFcmToken(String uniqueId, String token) {
        User user = userRepository.findById(uniqueId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        FcmToken fcmToken = FcmToken.builder()
                .user(user)
                .token(token)
                .build();

        fcmTokenRepository.save(fcmToken);
        return new FcmTokenResponseDto(token);
    }

    // 메시지 전송 (server -> firebase)
    public void sendMessageTo(String token, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        //firebase 서버로 전송할 메시지 생성
        Message message = Message.builder()
                .putData("returnUrl", "${NOTIFICATION_RETURN_URL}") //알림 클릭시 이동할 url도 보냄
                .setNotification(notification)
                .setToken(token)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            throw new CustomException(ErrorCode.FCM_SEND_FAIL);
        }
    }

    // Fcm Token들 불러오기
    public List<String> getFcmTokens(String uniqueId) {
        return fcmTokenRepository.findByUser_UniqueId(uniqueId).stream()
                .map(FcmToken::getToken)
                .toList();
    }
}

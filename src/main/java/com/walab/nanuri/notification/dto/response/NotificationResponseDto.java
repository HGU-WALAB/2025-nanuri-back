package com.walab.nanuri.notification.dto.response;

import com.walab.nanuri.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String title;
    private String body;
    private String itemId;
    private boolean isRead;
    private String fcmToken;
    private String relatedUrl;

    public static NotificationResponseDto from(Notification notification){
        return new NotificationResponseDto(
                notification.getId(),
                notification.getTitle(),
                notification.getBody(),
                notification.getItemId(),
                notification.isRead(),
                notification.getFcmToken(),
                notification.getRelatedUrl()
        );
    }
}

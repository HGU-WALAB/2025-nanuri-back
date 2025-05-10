package com.walab.nanuri.notification.dto.response;

import com.walab.nanuri.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@Builder
public class NotificationResponseDto {
    private Long id;
    private String title;
    private String body;
    private String itemId;
    private boolean isRead;
    private String createdTime;

    public static NotificationResponseDto from(Notification notification){
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .body(notification.getBody())
                .itemId(notification.getItemId())
                .isRead(notification.isRead())
                .createdTime(notification.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}

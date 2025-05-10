package com.walab.nanuri.notification.dto.request;

import com.walab.nanuri.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDto {
    private String receiverId; //알림 받을 유저
    private String title;
    private String body;
    private String itemId; //관련 Item ID
}

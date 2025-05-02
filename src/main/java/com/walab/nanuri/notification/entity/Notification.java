package com.walab.nanuri.notification.entity;


import com.walab.nanuri.commons.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String receiverId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    private String itemId;

    @Setter
    private boolean isRead; //알림 읽은 여부

    @Column(length = 512)
    private String fcmToken;

}

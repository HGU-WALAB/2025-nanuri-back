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

    //알림 읽은 여부
    @Setter
    private boolean isRead;

    @Column(length = 512)
    private String fcmToken;

    //알림 클릭 시 이동할 URL
    @Column(name = "related_url", length = 512)
    private String relatedUrl;

}

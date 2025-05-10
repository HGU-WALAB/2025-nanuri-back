package com.walab.nanuri.notification.entity;


import com.walab.nanuri.commons.entity.BaseTimeEntity;
import com.walab.nanuri.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", referencedColumnName = "unique_id", nullable = false)
    private User receiver;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    private String itemId;

    //알림 읽은 여부
    @Setter
    private boolean isRead;
}

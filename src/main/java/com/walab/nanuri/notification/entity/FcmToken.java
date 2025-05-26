package com.walab.nanuri.notification.entity;

import com.walab.nanuri.user.entity.User;
import javax.persistence.*;
import lombok. *;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FcmToken {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 512)
    private String token;
}

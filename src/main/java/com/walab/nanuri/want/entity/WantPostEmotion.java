package com.walab.nanuri.want.entity;

import com.walab.nanuri.commons.util.EmotionType;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WantPostEmotion {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private WantPost wantPost;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "emotion_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EmotionType emotionType;
}

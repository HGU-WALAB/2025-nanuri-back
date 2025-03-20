package com.walab.nanuri.item.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable=false)
    private Long id;

    @Column(nullable = false, length=30)
    private String title;

    @Column(length =200)
    private String description;

    @Column(length = 50)
    private String place;

    @Column(name="view_count")
    private Integer viewCount;

    @Column(length = 50)
    private String category;

    @Column(name="user_id", nullable=false)
    private Long userId;

    @Column(name="is_finished")
    private Boolean isFinished;

    @CreatedDate
    private LocalDateTime createdAt;
}

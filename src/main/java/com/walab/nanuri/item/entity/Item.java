package com.walab.nanuri.item.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Item {
    @Id
    @Column(name = "id", nullable=false, length=50)
    private long id;

    @Column(nullable = false, length=100)
    private String title;

    @Column(length = 255)
    private String description;

    @Column(length = 50)
    private String place;

    @Column(name="view_count")
    private Integer viewCount;

    @Column(length = 50)
    private String category;

    @Column(name="user_id", nullable=false)
    private long userId;

    @Column(name="is_finished")
    private Boolean isFinished;

    @CreatedDate
    private LocalDateTime createdAt;
}

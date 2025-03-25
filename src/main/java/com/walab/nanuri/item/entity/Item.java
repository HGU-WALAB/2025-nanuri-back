package com.walab.nanuri.item.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, length=30)
    private String title;

    @Column(length =150)
    private String description;

    @Column(length = 50)
    private String place;

    @Column(name="view_count")
    private Integer viewCount;

    @Column(length = 50)
    private String category;

    @Column(name="user_id", nullable=false)
    private String userId;

    @Column(name="is_finished")
    private Boolean isFinished;

    @CreatedDate
    private LocalDateTime createdTime;

    @LastModifiedDate
    private LocalDateTime updatedTime;

    @Column(name="wish_count")
    private Integer wishCount;

    @Column(name="is_owner")
    private Boolean isOwner;


    public void update(String title, String description, String place, String category) {
        this.title = title;
        this.description = description;
        this.place = place;
        this.category = category;
    }
}

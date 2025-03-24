package com.walab.nanuri.item.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Item extends BaseEntity {

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
    private Long userId;

    @Column(name="is_finished")
    private Boolean isFinished;

    public void update(String title, String description, String place, String category) {
        this.title = title;
        this.description = description;
        this.place = place;
        this.category = category;
    }
}

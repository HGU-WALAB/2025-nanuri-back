package com.walab.nanuri.item.entity;

import com.walab.nanuri.commons.entity.BaseTimeEntity;
import com.walab.nanuri.item.dto.request.ItemRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Item extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, length=30)
    private String title;

    @Column(length = 1024)
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

    @Column(name="wish_count")
    private Integer wishCount;


    public void update(String title, String description, String place, String category) {
        this.title = title;
        this.description = description;
        this.place = place;
        this.category = category;
    }

    public void markIsFinished(){
        this.isFinished = true;
    }

    public static Item toEntity(String userId, ItemRequestDto requestDto) {
        return Item.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .place(requestDto.getPlace())
                .category(requestDto.getCategory())
                .viewCount(0)
                .userId(userId)
                .isFinished(Boolean.FALSE)
                .build();

    }
}

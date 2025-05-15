package com.walab.nanuri.item.entity;

import com.walab.nanuri.commons.entity.BaseTimeEntity;
import com.walab.nanuri.commons.util.ItemCategory;
import com.walab.nanuri.commons.util.ShareStatus;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(length = 1024)
    private String description;

    @Column(length = 50)
    private String place;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "share_status")
    @Enumerated(EnumType.STRING)
    @Setter
    private ShareStatus shareStatus;

    @Column(name = "wish_count")
    @Setter
    private Integer wishCount;

    @Column(name = "chat_count")
    @Setter
    private Integer chatCount;

    @Column(name = "deadline")
    private LocalDateTime deadline;


    public void update(String title, String description, String place, String category, LocalDateTime deadline) {
        this.title = title;
        this.description = description;
        this.place = place;
        this.category = ItemCategory.valueOf(category);
        this.deadline = deadline;
    }

    public static Item toEntity(String userId, ItemRequestDto requestDto) {
        return Item.builder()
                .title(requestDto.getTitle())
                .description(requestDto.getDescription())
                .place(requestDto.getPlace())
                .category(ItemCategory.valueOf(requestDto.getCategory()))
                .viewCount(0)
                .userId(userId)
                .shareStatus(ShareStatus.NONE)
                .wishCount(0)
                .chatCount(0)
                .deadline(requestDto.getDeadline())
                .build();
    }

    //조회수 증가
    public void addViewCount() {
        this.viewCount = (this.viewCount == null)? 1 : this.viewCount + 1;
    }
}

package com.walab.nanuri.want.entity;

import com.walab.nanuri.commons.entity.BaseTimeEntity;
import com.walab.nanuri.commons.util.EmotionType;
import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.want.dto.request.WantPostRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "want_post")
public class WantPost extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Setter
    @Column(name = "description", nullable = false, length = 1024)
    private String description;

    @Column(name = "is_finished")
    private Boolean isFinished;

    @Column(name = "receiver_id")
    private String receiverId;

    @OneToMany(mappedBy = "wantPost", cascade = CascadeType.ALL)
    private List<WantPostSeller> sellers;

    @Setter
    @Column(name = "status")
    private ShareStatus status;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "need_it_count")
    private Integer needItCount;

    @Column(name = "cheering_count")
    private Integer cheeringCount;

    @Column(name = "amazing_count")
    private Integer amazingCount;


    public static WantPost toEntity(String receiverId, WantPostRequestDto dto) {
        return WantPost.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .receiverId(receiverId)
                .sellers(new ArrayList<>())
                .isFinished(false)
                .status(ShareStatus.NONE)
                .viewCount(0)
                .needItCount(0)
                .cheeringCount(0)
                .amazingCount(0)
                .build();
    }

    public void setFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public void addViewCount() {
        this.viewCount = (this.viewCount == null)? 1 : this.viewCount + 1;
    }

    public void addEmotionCount(EmotionType emotionType){
        switch (emotionType) {
            case NEED -> this.needItCount = (this.needItCount == null) ? 1 : this.needItCount + 1;
            case CHEERING -> this.cheeringCount = (this.cheeringCount == null) ? 1 : this.cheeringCount + 1;
            case AMAZING -> this.amazingCount = (this.amazingCount == null) ? 1 : this.amazingCount + 1;
        }
    }

    public void minusEmotionCount(EmotionType emotionType){
        switch (emotionType) {
            case NEED -> this.needItCount = (this.needItCount == null) ? 0 : this.needItCount - 1;
            case CHEERING -> this.cheeringCount = (this.cheeringCount == null) ? 0 : this.cheeringCount - 1;
            case AMAZING -> this.amazingCount = (this.amazingCount == null) ? 0 : this.amazingCount - 1;
        }
    }
}

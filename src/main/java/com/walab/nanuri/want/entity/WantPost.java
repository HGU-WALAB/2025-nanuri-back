package com.walab.nanuri.want.entity;

import com.walab.nanuri.commons.entity.BaseTimeEntity;
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


    public static WantPost toEntity(WantPostRequestDto dto, String receiverId) {
        return WantPost.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .receiverId(receiverId)
                .sellers(new ArrayList<>())
                .isFinished(false)
                .status(ShareStatus.NONE)
                .viewCount(0)
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
}

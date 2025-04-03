package com.walab.nanuri.want.entity;

import com.walab.nanuri.commons.entity.BaseTimeEntity;
import com.walab.nanuri.commons.entity.ShareStatus;
import com.walab.nanuri.want.dto.request.WantPostRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", nullable = false, length = 1024)
    private String description;

    @Column(name = "is_finished")
    private Boolean isFinished;

    @Column(name = "receiver_id")
    private String receiverId;

    @OneToMany(mappedBy = "wantPost", cascade = CascadeType.ALL)
    private List<WantPostSeller> sellers;

    @Column(name = "status")
    private ShareStatus status;

    public static WantPost toEntity(WantPostRequestDto dto, String receiverId) {
        return WantPost.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .receiverId(receiverId)
                .sellers(new ArrayList<>())
                .isFinished(false)
                .status(ShareStatus.NONE)
                .build();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public void setStatus(ShareStatus status) {
        this.status = status;
    }

    public boolean isFinished() {
        return this.isFinished;
    }
}

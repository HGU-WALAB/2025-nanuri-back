package com.walab.nanuri.history.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class History {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name="get_user_id")
    private String getUserId;

    @Column(name="is_selected")
    private Boolean isSelected;

    @Column(name="is_confirmed")
    private Boolean isConfirmed;

    @CreatedDate
    private LocalDateTime createdTime;

    public static History toEntity(String userId, Long itemId) {
        return History.builder()
                .itemId(itemId)
                .getUserId(userId)
                .isSelected(false)
                .isConfirmed(false)
                .build();
    }

    public void markConfirmed() {
        this.isConfirmed = true;
    }

    public void markSelected() {
        this.isSelected = true;
    }

    public void markUnSelected() {
        this.isSelected = false;
    }
}

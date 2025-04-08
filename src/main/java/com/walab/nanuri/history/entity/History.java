package com.walab.nanuri.history.entity;

import com.walab.nanuri.commons.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class History extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name="received_id")
    private String receivedId;

    @Column(name="is_confirmed")
    private Boolean isConfirmed;

    public static History toEntity(String userId, Long itemId) {
        return History.builder()
                .itemId(itemId)
                .receivedId(userId)
                .isConfirmed(false)
                .build();
    }

    public void markConfirmed() {
        this.isConfirmed = true;
    }
}

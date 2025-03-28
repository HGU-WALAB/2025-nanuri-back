package com.walab.nanuri.history.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @Column(name="is_finished")
    private Boolean isFinished;

    @CreatedDate
    private String applicationTime;

}

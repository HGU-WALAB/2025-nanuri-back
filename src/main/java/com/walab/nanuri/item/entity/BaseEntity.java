package com.walab.nanuri.item.entity;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public abstract class BaseEntity {

    @CreatedDate
    private String createdDate;

    @LastModifiedDate
    private String lastModifiedDate;

    //Entity가 DB에 insert 되기 전 호출
    @PrePersist
    public void onPrePersist() {
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        this.lastModifiedDate = this.createdDate;
    }

    //Entity가 DB에 Update 되기 전 호출
    @PreUpdate
    public void onPreUpdate() {
        this.lastModifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:"));
    }


}

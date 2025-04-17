package com.walab.nanuri.alarm.repository;

import com.walab.nanuri.alarm.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReceiverIdOrderByCreatedTimeDesc(String receiverId);
}

package com.walab.nanuri.notification.repository;

import com.walab.nanuri.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    //uniqueId로 유저 찾은 후 최신 알림을 위로 정렬하여 반환
    List<Notification> findByReceiver_UniqueIdOrderByCreatedTimeDesc(String uniqueId);
}

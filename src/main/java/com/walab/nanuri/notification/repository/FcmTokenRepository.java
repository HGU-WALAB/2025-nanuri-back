package com.walab.nanuri.notification.repository;

import com.walab.nanuri.notification.entity.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    List<FcmToken> findByUser_UniqueId(String uniqueId);
}

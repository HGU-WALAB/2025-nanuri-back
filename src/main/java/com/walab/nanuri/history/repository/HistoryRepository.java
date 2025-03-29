package com.walab.nanuri.history.repository;

import com.walab.nanuri.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {

    boolean existsByItemIdAndGetUserId(Long itemId, String getUserId);
    List<History> findByItemId(Long itemId);
    List<History> findAllByGetUserIdAndIsConfirmedTrue(String getUserId);
    List<History> findAllByGetUserIdAndIsConfirmedFalse(String getUserId);

}

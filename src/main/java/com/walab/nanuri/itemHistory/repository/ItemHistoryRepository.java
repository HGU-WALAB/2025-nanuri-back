package com.walab.nanuri.itemHistory.repository;

import com.walab.nanuri.itemHistory.entity.ItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemHistoryRepository extends JpaRepository<ItemHistory, Long> {

    boolean existsByItemIdAndGetUserId(Long itemId, String getUserId);
    Optional<ItemHistory> findByItemIdAndGetUserId(Long itemId, String getUserId);
    void deleteByItemIdAndGetUserId(Long itemId, String getUserId);

}

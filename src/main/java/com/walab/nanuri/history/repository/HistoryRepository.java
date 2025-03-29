package com.walab.nanuri.history.repository;

import com.walab.nanuri.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {

    boolean existsByItemIdAndGetUserId(Long itemId, String getUserId);
    Optional<History> findByItemIdAndGetUserId(Long itemId, String getUserId);
    void deleteByItemIdAndGetUserId(Long itemId, String getUserId);
    List<History> findAllByGetUserIdAndIsConfirmedTrue(String getUserId);

}

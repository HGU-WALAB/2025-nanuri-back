package com.walab.nanuri.history.repository;

import com.walab.nanuri.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {

    boolean existsByItemIdAndReceivedId(Long itemId, String receivedId);
    boolean existsByIdAndReceivedId(Long id, String receivedId);
    List<History> findByItemId(Long itemId);
    List<History> findAllByReceivedIdAndIsConfirmedTrue(String receivedId);
    List<History> findAllByReceivedIdAndIsConfirmedFalse(String receivedId);

}

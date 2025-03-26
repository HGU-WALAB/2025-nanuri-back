package com.walab.nanuri.itemHistory.repository;

import com.walab.nanuri.itemHistory.entity.ItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemHistoryRepository extends JpaRepository<ItemHistory, Long> {
}

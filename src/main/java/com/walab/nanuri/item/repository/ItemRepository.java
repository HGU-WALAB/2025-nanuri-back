package com.walab.nanuri.item.repository;

import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByCategory(String category);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.userId = :userId AND i.shareStatus = :shareStatus")
    List<Item> findAllByUserIdAndIsFinished(String userId, ShareStatus shareStatus);

    List<Item> findAllByUserId(String userId);
}

package com.walab.nanuri.item.repository;

import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category " +
            "ORDER BY i.id DESC")
    List<Item> findAllByCategoryOrdered(@Param("category") String category);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.userId = :userId AND i.shareStatus = :shareStatus " +
            "ORDER BY i.id DESC")
    List<Item> findAllByUserIdAndShareStatus(String userId, ShareStatus shareStatus);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.userId = :userId " +
            "ORDER BY i.id DESC")
    List<Item> findAllByUserId(@Param("userId") String userId);

    @Query("SELECT i " +
            "FROM Item i " +
            "ORDER BY i.id DESC")
    List<Item> findAllOrdered();
}

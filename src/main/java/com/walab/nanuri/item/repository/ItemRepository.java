package com.walab.nanuri.item.repository;

import com.walab.nanuri.commons.util.ItemCategory;
import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category " +
            "ORDER BY i.id DESC")
    List<Item> findAllByCategoryOrdered(@Param("category") ItemCategory category);

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

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.title LIKE CONCAT('%', :title, '%') " +
            "ORDER BY i.id DESC")
    List<Item> findByTitleContaining(@Param("title") String title);

    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category " +
            "AND i.title LIKE CONCAT('%', :title, '%') " +
            "ORDER BY i.id DESC")
    List<Item> findByTitleContainingAndCategoryOrdered(@Param("title") String title, @Param("category") ItemCategory category);

    // 내일 나눔 마감인 아이템 조회
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE DATE(i.deadline) = :tomorrow")
    List<Item> findItemsDueTomorrow(@Param("tomorrow")Date tomorrow);
}

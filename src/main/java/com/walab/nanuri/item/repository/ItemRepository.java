package com.walab.nanuri.item.repository;

import com.walab.nanuri.commons.util.ItemCategory;
import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 카테고리로 조회
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category " +
            "ORDER BY i.id DESC")
    List<Item> findAllByCategoryOrdered(@Param("category") ItemCategory category);

    // 유저아이디와 나눔상태로 아이템 조회
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.userId = :userId AND i.shareStatus = :shareStatus " +
            "ORDER BY i.id DESC")
    List<Item> findAllByUserIdAndShareStatus(String userId, ShareStatus shareStatus);

    //유저아이디로 아이템 조회
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.userId = :userId " +
            "ORDER BY i.id DESC")
    List<Item> findAllByUserId(@Param("userId") String userId);

    // 최신순 정렬
    @Query("SELECT i " +
            "FROM Item i " +
            "ORDER BY i.id DESC")
    List<Item> findAllOrdered();

    // 내일 나눔 마감인 아이템 조회
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE DATE(i.deadline) = :tomorrow")
    List<Item> findItemsDueTomorrow(@Param("tomorrow") Date tomorrow);

    // keyword로 제목, 내용, 닉네임 검색
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.title LIKE %:keyword% OR " +
            "i.description LIKE %:keyword% OR " +
            "i.userId IN (SELECT u.uniqueId FROM User u WHERE u.nickname LIKE %:keyword%) " +
            "ORDER BY i.id DESC")
    List<Item> searchByKeyword(@Param("keyword") String keyword);

    // keyword로 제목, 내용, 닉네임 검색 + 카테고리 검색
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.title LIKE %:keyword% OR " +
            "i.description LIKE %:keyword% OR " +
            "i.userId IN (SELECT u.uniqueId FROM User u WHERE u.nickname LIKE %:keyword%)" +
            "AND i.category = :category " +
            "ORDER BY i.id DESC")
    List<Item> searchByKeywordAndCategory(@Param("keyword") String keyword, @Param("category") ItemCategory category);

}

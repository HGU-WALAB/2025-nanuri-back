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


    // Item 정렬 Query

    // 최신순 정렬
    @Query("SELECT i " +
            "FROM Item i " +
            "ORDER BY i.id DESC")
    List<Item> findAllOrderedByLatest();

    // 오래된 순 정렬
    @Query("SELECT i " +
            "FROM Item i " +
            "ORDER BY i.id ASC")
    List<Item> findAllOrderedByOldest();

    // 조회순 정렬
    @Query("SELECT i " +
            "FROM Item i " +
            "ORDER BY i.viewCount DESC")
    List<Item> findAllByViewCountOrdered();

    // 관심순 정렬
    @Query("SELECT i " +
            "FROM Item i " +
            "ORDER BY i.wishCount DESC")
    List<Item> findAllByWishCountOrdered();



    // 마감임박 순 정렬
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.shareStatus = 'NONE' " +
            "ORDER BY i.deadline ASC")
    List<Item> findAllByDeadlineOrdered();

    // 카테고리 + 최신순 정렬
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category " +
            "ORDER BY i.id DESC")
    List<Item> findAllByCategoryOrderedLatest(@Param("category") ItemCategory category);

    // 카테고리 + 오래된 순 정렬
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category " +
            "ORDER BY i.id ASC")
    List<Item> findAllByCategoryOrderedOldest(@Param("category") ItemCategory category);

    // 카테고리 + 조회순 정렬
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category " +
            "ORDER BY i.viewCount DESC")
    List<Item> findAllByCategoryOrderedByViewCount(@Param("category") ItemCategory category);

    // 카테고리 + 관심순 정렬
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category " +
            "ORDER BY i.wishCount DESC")
    List<Item> findAllByCategoryOrderedByWishCount(@Param("category") ItemCategory category);

    // 카테고리 + 마감임박 순 정렬
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category AND i.shareStatus = 'NONE' " +
            "ORDER BY i.deadline ASC")
    List<Item> findAllByCategoryOrderedByDeadline(@Param("category") ItemCategory category);
}

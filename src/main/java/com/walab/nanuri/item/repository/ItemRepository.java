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


    // 제목으로 아이템 조회
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.title LIKE CONCAT('%', :title, '%') " +
            "ORDER BY i.id DESC")
    List<Item> findByTitleContaining(@Param("title") String title);

    // 제목과 카테고리로 아이템 조회
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category " +
            "AND i.title LIKE CONCAT('%', :title, '%') " +
            "ORDER BY i.id DESC")
    List<Item> findByTitleContainingAndCategoryOrdered(@Param("title") String title, @Param("category") ItemCategory category);


    // 내용으로 아이템 검색
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.description LIKE CONCAT('%', :description, '%') " +
            "ORDER BY i.id DESC")
    List<Item> findByDescriptionContaining(@Param("description") String description);

    // 내용과 카테고리로 아이템 검색
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category " +
            "AND i.description LIKE CONCAT('%', :description, '%') " +
            "ORDER BY i.id DESC")
    List<Item> findByDescriptionContainingAndCategoryOrdered(@Param("description") String description, @Param("category") ItemCategory category);

    // 제목과 내용으로 아이템 검색
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.title LIKE CONCAT('%', :title, '%') " +
            "AND i.description LIKE CONCAT('%', :description, '%') " +
            "ORDER BY i.id DESC")
    List<Item> findByTitleAndDescriptionContaining(@Param("title") String title, @Param("description") String description);

    // 제목, 내용, 카테고리로 아이템 검색
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.category = :category " +
            "AND i.title LIKE CONCAT('%', :title, '%') " +
            "AND i.description LIKE CONCAT('%', :description, '%') " +
            "ORDER BY i.id DESC")
    List<Item> findByTitleAndDescriptionContainingAndCategoryOrdered(@Param("title") String title, @Param("description") String description, @Param("category") ItemCategory category);

    // 닉네임으로 아이템 검색
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.userId IN (SELECT u.uniqueId FROM User u WHERE u.nickname LIKE CONCAT('%', :nickname, '%')) " +
            "ORDER BY i.id DESC")
    List<Item> findByNicknameContaining(@Param("nickname") String nickname);

    // 닉네임과 카테고리로 아이템 검색
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE i.userId IN (SELECT u.uniqueId FROM User u WHERE u.nickname LIKE CONCAT('%', :nickname, '%')) " +
            "AND i.category = :category " +
            "ORDER BY i.id DESC")
    List<Item> findByNicknameContainingAndCategoryOrdered(@Param("nickname") String nickname, @Param("category") ItemCategory category);

    // 내일 나눔 마감인 아이템 조회
    @Query("SELECT i " +
            "FROM Item i " +
            "WHERE DATE(i.deadline) = :tomorrow")
    List<Item> findItemsDueTomorrow(@Param("tomorrow") Date tomorrow);
}

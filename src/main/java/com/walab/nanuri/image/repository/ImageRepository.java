package com.walab.nanuri.image.repository;

import com.walab.nanuri.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query(value = "SELECT * FROM images WHERE item_id = :itemId ORDER BY id ASC LIMIT 1", nativeQuery = true)
    Image findTopByItemIdOrderByIdAsc(@Param("itemId") Long itemId);

    List<Image> findByItemIdOrderByIdAsc(@Param("itemId") Long itemId);

    @Query("SELECT i FROM images i WHERE i.item.id IN :itemIds AND i.id IN (" +
            "SELECT MIN(i2.id) FROM images i2 WHERE i2.item.id IN :itemIds GROUP BY i2.item.id)")
    List<Image> findFirstImagePerItem(@Param("itemIds") List<Long> itemIds);
}

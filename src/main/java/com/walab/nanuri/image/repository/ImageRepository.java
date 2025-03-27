package com.walab.nanuri.image.repository;

import com.walab.nanuri.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM images i WHERE i.item.id = :itemId ORDER BY i.id ASC LIMIT 1")
    Image findTopByItemIdOrderByIdAsc(@Param("itemId") Long itemId);

}

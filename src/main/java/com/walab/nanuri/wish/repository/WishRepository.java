package com.walab.nanuri.wish.repository;

import com.walab.nanuri.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Optional<Wish> findWishByUniqueIdAndItemId(String uniqueId, Long itemId);
    Optional<Wish> findWishByUniqueIdAndId(String uniqueId, Long id);
    List<Wish> findAllByUniqueId(String uniqueId);
}

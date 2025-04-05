package com.walab.nanuri.wish.repository;

import com.walab.nanuri.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findAllByUniqueId(String uniqueId);
}

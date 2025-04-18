package com.walab.nanuri.wish.repository;

import com.walab.nanuri.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findAllByUniqueId(String uniqueId);
    int countByItemId(Long itemId); //관심목록 추가한 사람의 수 계산
}

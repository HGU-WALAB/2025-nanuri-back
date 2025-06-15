package com.walab.nanuri.wish.repository;

import com.walab.nanuri.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    List<Wish> findAllByUniqueId(String uniqueId);
    int countByItemId(Long itemId); //관심목록 추가한 사람의 수 계산
    boolean existsByUniqueIdAndItemId(String uniqueId, Long itemId); //관심목록에 추가된 아이템인지 확인
    Optional<Wish> findByUniqueIdAndItemId(String uniqueId, Long itemId);
    List<Wish> findAllByItemId(Long itemId); //특정 아이템에 대한 모든 관심 목록 조회

}

package com.walab.nanuri.want.repository;

import com.walab.nanuri.commons.util.EmotionType;
import com.walab.nanuri.want.entity.WantPostEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WantPostEmotionRepository extends JpaRepository<WantPostEmotion, Long> {
    boolean existsByUserIdAndWantPostId(String userId, Long postId);
    Optional<WantPostEmotion> findByUserIdAndWantPostId(String userId, Long postId);

    @Query("SELECT w.emotionType AS emotionType, COUNT(w) AS count " +
            "FROM WantPostEmotion w WHERE w.wantPost.id = :postId " +
            "GROUP BY w.emotionType")
    List<Object[]>  countEmotionsByPostId(Long postId);

    @Query("SELECT w.emotionType FROM WantPostEmotion w " +
            "WHERE w.wantPost.id = :postId AND w.userId = :userId")
    Set<EmotionType> findEmotionTypesByUserIdAndPostId(String userId, Long postId);
}

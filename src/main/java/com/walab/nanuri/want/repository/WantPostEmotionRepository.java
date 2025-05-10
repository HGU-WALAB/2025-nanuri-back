package com.walab.nanuri.want.repository;

import com.walab.nanuri.commons.util.EmotionType;
import com.walab.nanuri.want.entity.WantPostEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface WantPostEmotionRepository extends JpaRepository<WantPostEmotion, Long> {
    boolean existsByUserIdAndWantPostIdAndEmotionType(String userId, Long postId, EmotionType emotionType);

    //post에 달린 emotion들 가져오기
    Optional<WantPostEmotion> findByUserIdAndWantPostIdAndEmotionType(String userId, Long postId, EmotionType emotionType);

    // emotion type별 갯수 count
    @Query("SELECT w.emotionType AS emotionType, COUNT(w) AS count " +
            "FROM WantPostEmotion w WHERE w.wantPost.id = :postId " +
            "GROUP BY w.emotionType")
    List<Object[]>  countEmotionsByPostId(Long postId);

    // emotion type 가져오기
    @Query("SELECT w.emotionType FROM WantPostEmotion w " +
            "WHERE w.wantPost.id = :postId AND w.userId = :userId")
    Set<EmotionType> findEmotionTypesByUserIdAndPostId(String userId, Long postId);
}

package com.walab.nanuri.want.repository;

import com.walab.nanuri.want.entity.WantPost;
import com.walab.nanuri.want.entity.WantPostEmotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WantPostRepository extends JpaRepository<WantPost, Long> {
    @Query("SELECT w FROM want_post w ORDER BY w.createdTime DESC")
    List<WantPost> findAllOrderByCreatedTimeDesc();
}

package com.walab.nanuri.want.repository;

import com.walab.nanuri.want.entity.WantPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WantPostRepository extends JpaRepository<WantPost, Long> {
    @Query("SELECT w FROM want_post w ORDER BY w.modifiedDate DESC")
    List<WantPost> findAllOrderByModifiedDateDesc();
}

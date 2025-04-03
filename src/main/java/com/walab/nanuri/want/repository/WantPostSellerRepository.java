package com.walab.nanuri.want.repository;

import com.walab.nanuri.want.entity.WantPostSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WantPostSellerRepository extends JpaRepository<WantPostSeller, Long> {
}
package com.walab.nanuri.image.repository;

import com.walab.nanuri.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}

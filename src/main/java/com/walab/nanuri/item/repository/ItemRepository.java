package com.walab.nanuri.item.repository;

import com.walab.nanuri.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}

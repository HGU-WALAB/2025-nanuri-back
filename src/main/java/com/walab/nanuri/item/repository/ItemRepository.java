package com.walab.nanuri.item.repository;

import com.walab.nanuri.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Locale;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCategory(String category);
}

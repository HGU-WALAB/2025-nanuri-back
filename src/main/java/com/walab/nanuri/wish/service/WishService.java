package com.walab.nanuri.wish.service;

import com.walab.nanuri.commons.exception.WishNotExistException;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.wish.dto.response.WishResponseDto;
import com.walab.nanuri.wish.entity.Wish;
import com.walab.nanuri.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void createWish(String uniqueId, Long itemId) {
        Wish wish = Wish.builder()
                .uniqueId(uniqueId)
                .itemId(itemId)
                .build();

        wishRepository.save(wish);
    }

    @Transactional
    public void deleteWish(String uniqueId, Long itemId) {
        wishRepository.delete(
                wishRepository.findWishByUniqueIdAndItemId(uniqueId, itemId)
                        .orElseThrow(WishNotExistException::new)
        );
    }

    public List<WishResponseDto> getAllWish(String uniqueId) {
        List<Wish> wishes = wishRepository.findAllByUniqueId(uniqueId);
        List<Long> itemIds = wishes.stream().map(Wish::getItemId).toList();
        List<Item> items = itemRepository.findAllById(itemIds);
        Map<Long, Item> itemMap = items.stream()
                .collect(Collectors.toMap(Item::getId, item -> item));

        return wishes.stream()
                .map(wish -> WishResponseDto.createDefaultDto(wish, itemMap.get(wish.getItemId())))
                .toList();
    }
}

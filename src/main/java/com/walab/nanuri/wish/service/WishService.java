package com.walab.nanuri.wish.service;

import com.walab.nanuri.commons.exception.ItemNotExistException;
import com.walab.nanuri.commons.exception.WishNotExistException;
import com.walab.nanuri.image.repository.ImageRepository;
import com.walab.nanuri.item.dto.response.ItemListResponseDto;
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
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    public void createWish(String uniqueId, Long itemId) {
        if(!itemRepository.existsById(itemId)) {
            throw new ItemNotExistException();
        }

        Wish wish = Wish.builder()
                .uniqueId(uniqueId)
                .itemId(itemId)
                .build();

        wishRepository.save(wish);
    }

    public void deleteWish(String uniqueId, Long itemId) { // TODO
        wishRepository.delete(
                wishRepository.findWishByUniqueIdAndItemId(uniqueId, itemId)
                        .orElseThrow(WishNotExistException::new)
        );
    }

    public List<WishResponseDto> getWishList(String uniqueId) {
        List<Wish> wishes = wishRepository.findAllByUniqueId(uniqueId);
        List<Long> itemIds = wishes.stream().map(Wish::getItemId).toList();
        List<Item> items = itemRepository.findAllById(itemIds);

        List<ItemListResponseDto> dtos = items.stream()
                .map(item -> {
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();

                    return ItemListResponseDto.from(item, image);
                })
                .toList();

        Map<Long, ItemListResponseDto> itemDtoMap = dtos.stream()
                .collect(Collectors.toMap(ItemListResponseDto::getItemId, dto -> dto));

        return wishes.stream()
                .map(wish -> {
                    ItemListResponseDto itemDto = itemDtoMap.get(wish.getItemId());
                    return WishResponseDto.createDefaultDto(wish, itemDto);
                })
                .toList();
    }
}

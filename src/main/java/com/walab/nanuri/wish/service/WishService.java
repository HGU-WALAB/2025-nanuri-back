package com.walab.nanuri.wish.service;

import com.walab.nanuri.commons.exception.CustomException;
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

import static com.walab.nanuri.commons.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class WishService {

    private final WishRepository wishRepository;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public void createWish(String uniqueId, Long itemId) {
        if(!itemRepository.existsById(itemId)) {
            throw new CustomException(ITEM_NOT_FOUND);
        }



        Wish wish = Wish.builder()
                .uniqueId(uniqueId)
                .itemId(itemId)
                .build();

        wishRepository.save(wish);
    }

    @Transactional
    public void deleteWish(Long wishId) {
        wishRepository.delete(
                wishRepository.findById(wishId)
                        .orElseThrow(() -> new CustomException(MISSING_WISH))
        );
    }

    public List<WishResponseDto> getWishList(String uniqueId) {
        List<Wish> wishes = wishRepository.findAllByUniqueId(uniqueId);
        List<Long> itemIds = wishes.stream().map(Wish::getItemId).toList();
        List<Item> items = itemRepository.findAllById(itemIds);

        Map<Long, ItemListResponseDto> itemDtoMap = items.stream()
                .collect(Collectors.toMap(
                        Item::getId,
                        item -> {
                            String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                                    .getFileUrl();
                            return ItemListResponseDto.from(item, image);
                        }
                ));

        return wishes.stream()
                .map(wish -> {
                    ItemListResponseDto itemDto = itemDtoMap.get(wish.getItemId());
                    return WishResponseDto.from(wish, itemDto);
                })
                .toList();
    }
}

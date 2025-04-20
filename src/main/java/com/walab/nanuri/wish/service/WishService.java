package com.walab.nanuri.wish.service;

import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.image.entity.Image;
import com.walab.nanuri.image.repository.ImageRepository;
import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import com.walab.nanuri.user.service.UserService;
import com.walab.nanuri.wish.dto.response.WishResponseDto;
import com.walab.nanuri.wish.entity.Wish;
import com.walab.nanuri.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.walab.nanuri.commons.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class WishService {
    private final WishRepository wishRepository;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    //관심 목록 추가
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

        //wishCount 증가
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));
        Integer currentWishCount = item.getWishCount() != null ? item.getWishCount() : 0;
        item.setWishCount(currentWishCount + 1);
        item.setWishStatus(true);
    }

    //관심 목록 삭제
    @Transactional
    public void deleteWish(Long wishId) {
        wishRepository.delete(
                wishRepository.findById(wishId)
                        .orElseThrow(() -> new CustomException(MISSING_WISH))
        );

        //wishCount 감소
        Item item = itemRepository.findById(wishId)
                .orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));
        Integer currentWishCount = item.getWishCount() != null ? item.getWishCount() : 0;
        item.setWishCount(Math.max(0, currentWishCount - 1));
        item.setWishStatus(false);
    }

    //관심 목록 전체 조회
    public List<WishResponseDto> getWishList(String uniqueId) {
        List<Wish> wishes = wishRepository.findAllByUniqueId(uniqueId);
        List<Long> itemIds = wishes.stream().map(Wish::getItemId).toList();
        List<Item> items = itemRepository.findAllById(itemIds);

        Map<Long, Item> itemMap = items.stream().
                collect(Collectors.toMap(
                        Item::getId,
                        Function.identity()
                ));
        Map<Long, String> imageMap = imageRepository.findFirstImagePerItem(itemIds).stream()
                .collect(Collectors.toMap(
                        image -> image.getItem().getId(),
                        Image::getFileUrl
                ));

        List<String> userIds = items.stream().map(Item::getUserId).distinct().toList();
        Map<String, String> userNameMap = userRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(
                        User::getUniqueId,
                        User::getName
                ));

        return wishes.stream()
                .map(wish -> {
                    Item item = itemMap.get(wish.getItemId());
                    String image = imageMap.get(item.getId());
                    String nickname = userNameMap.get(item.getUserId());

                    ItemListResponseDto itemDto = ItemListResponseDto.from(item, image, nickname);
                    return WishResponseDto.from(wish, itemDto);
                })
                .toList();
    }
}

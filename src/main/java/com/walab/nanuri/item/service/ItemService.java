package com.walab.nanuri.item.service;

import com.walab.nanuri.chat.service.ChatRoomService;
import com.walab.nanuri.commons.util.ItemCategory;
import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.image.entity.Image;
import com.walab.nanuri.image.repository.ImageRepository;
import com.walab.nanuri.image.service.ImageService;
import com.walab.nanuri.item.dto.request.ItemRequestDto;
import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.item.dto.response.ItemResponseDto;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import com.walab.nanuri.wish.entity.Wish;
import com.walab.nanuri.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.walab.nanuri.commons.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ChatRoomService chatRoomService;
    private final ImageService imageService;
    private final WishRepository wishRepository;

    //Item 추가
    @Transactional
    public Long createItem(String uniqueId, ItemRequestDto itemDto) {
        Item item = Item.toEntity(uniqueId, itemDto);
        itemRepository.save(item);

        return item.getId();
    }

    //Item 전체 조회(일반 전체 조회, 카테고리 선택 후 전체 조회)
    public List<ItemListResponseDto> getAllItems(String uniqueId, String category) {
        List<Item> items = category.isEmpty() ?
                itemRepository.findAllOrdered() : itemRepository.findAllByCategoryOrdered(ItemCategory.valueOf(category));

        List<Long> wishItemIds;
        if (!uniqueId.isEmpty()) {
            wishItemIds = wishRepository.findAllByUniqueId(uniqueId).stream()
                    .map(Wish::getItemId)
                    .collect(Collectors.toList());
        } else {
            wishItemIds = List.of();
        }

        return items.stream()
                .map(item -> {
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    String nickname = getUserNicknameById(item.getUserId());
                    boolean wishStatus = wishItemIds.contains(item.getId());

                    return ItemListResponseDto.from(item, image, nickname, wishStatus);
                })
                .toList();
    }

    //Item 단건 조회
    @Transactional
    public ItemResponseDto getItemById(String uniqueId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));
        List<String> imageUrls = imageRepository.findByItemIdOrderByIdAsc(itemId).stream()
                .map(Image::getFileUrl)
                .collect(Collectors.toList());

        String nickname = getUserNicknameById(item.getUserId());
        boolean isOwner = item.getUserId().equals(uniqueId);
        boolean wishStatus = wishRepository.existsByUniqueIdAndItemId(uniqueId, itemId);

        item.addViewCount(); // 조회수 증가
        return ItemResponseDto.from(item, imageUrls, isOwner, nickname, wishStatus);
    }

    //다른 User의 Item 전체 조회
    public List<ItemListResponseDto> getItemsByUserNickname(String nickname) {
        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        List<Item> items = itemRepository.findAllByUserId(user.getUniqueId());

        return items.stream()
                .map(item -> {
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    boolean wishStatus = wishRepository.existsByUniqueIdAndItemId(user.getUniqueId(), item.getId());
                    return ItemListResponseDto.from(item, image, nickname, wishStatus);
                })
                .toList();
    }

    //나눔 중 혹은 완료된 나의 Item 조회
    public List<ItemListResponseDto> getOngoingMyItems(String uniqueId, String done) {
        if (uniqueId.isEmpty()) {
            return List.of();
        }

        ShareStatus upper_done;
        try {
            upper_done = ShareStatus.valueOf(done.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(INVALID_SHARE_STATUS);
        }
        List<Item> items = itemRepository.findAllByUserIdAndShareStatus(uniqueId, upper_done);

        return items.stream()
                .map(item -> {
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    String nickname = getUserNicknameById(item.getUserId());
                    boolean wishStatus = wishRepository.existsByUniqueIdAndItemId(uniqueId, item.getId());
                    return ItemListResponseDto.from(item, image, nickname, wishStatus);
                })
                .toList();
    }

    //Item 수정
    @Transactional
    public void updateItem(String uniqueId, Long updateId, ItemRequestDto itemDto) {
        Item findItem = itemRepository.findById(updateId).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));

        if(findItem.getUserId().equals(uniqueId)) { // 아이템 주인이 맞을 경우
            findItem.update(itemDto.getTitle(), itemDto.getDescription(), itemDto.getPlace(), itemDto.getCategory(), itemDto.getDeadline());
        } else {
            throw new CustomException(VALID_ITEM);
        }
    }


    //Item 삭제하기
    @Transactional
    public void deleteItem(String uniqueId, Long itemId) {
        Item findItem = itemRepository.findById(itemId).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));
        if(findItem.getUserId().equals(uniqueId)) {
            chatRoomService.deleteChatRoomsByItemId(itemId);
            imageService.deleteImages(itemId);
            itemRepository.delete(findItem);
        } else {
            throw new CustomException(VALID_ITEM);
        }
    }

    private String getUserNicknameById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND))
                .getNickname();
    }

    //제목으로 아이템 검색
    public List<ItemListResponseDto> getSearchTitleItems(String uniqueId, String title, String category) {
        List<Item> items = category.isEmpty() ?
                itemRepository.findByTitleContaining(title) : itemRepository.findByTitleContainingAndCategoryOrdered(title, ItemCategory.valueOf(category));

        return items.stream()
                .map(item -> {
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    boolean wishStatus = false;
                    if (uniqueId != null && !uniqueId.isEmpty()) {
                        wishStatus = wishRepository.existsByUniqueIdAndItemId(uniqueId, item.getId());
                    }
                    String nickname = getUserNicknameById(item.getUserId());
                    return ItemListResponseDto.from(item, image, nickname, wishStatus);
                })
                .toList();
    }

    //내용으로 아이템 검색
    public List<ItemListResponseDto> getSearchDescriptionItems(String uniqueId, String description, String category) {
        List<Item> items = category.isEmpty() ?
                itemRepository.findByDescriptionContaining(description) : itemRepository.findByDescriptionContainingAndCategoryOrdered(description, ItemCategory.valueOf(category));

        return items.stream()
                .map(item -> {
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    boolean wishStatus = false;
                    if (uniqueId != null && !uniqueId.isEmpty()) {
                        wishStatus = wishRepository.existsByUniqueIdAndItemId(uniqueId, item.getId());
                    }
                    String nickname = getUserNicknameById(item.getUserId());
                    return ItemListResponseDto.from(item, image, nickname, wishStatus);
                })
                .toList();
    }

    //제목과 내용으로 아이템 검색
    public List<ItemListResponseDto> getSearchTitleAndDescriptionItems(String uniqueId, String title, String description, String category) {
        List<Item> items = category.isEmpty() ?
                itemRepository.findByTitleAndDescriptionContaining(title, description) : itemRepository.findByTitleAndDescriptionContainingAndCategoryOrdered(title, description, ItemCategory.valueOf(category));

        return items.stream()
                .map(item -> {
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    boolean wishStatus = false;
                    if (uniqueId != null && !uniqueId.isEmpty()) {
                        wishStatus = wishRepository.existsByUniqueIdAndItemId(uniqueId, item.getId());
                    }
                    String nickname = getUserNicknameById(item.getUserId());
                    return ItemListResponseDto.from(item, image, nickname, wishStatus);
                })
                .toList();
    }

    //닉네임으로 아이템 검색
    public List<ItemListResponseDto> getSearchNicknameItems(String uniqueId, String nickname, String category) {
        List<Item> items = category.isEmpty() ?
                itemRepository.findByNicknameContaining(nickname) : itemRepository.findByNicknameContainingAndCategoryOrdered(nickname, ItemCategory.valueOf(category));

        return items.stream()
                .map(item -> {
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    boolean wishStatus = false;
                    if (uniqueId != null && !uniqueId.isEmpty()) {
                        wishStatus = wishRepository.existsByUniqueIdAndItemId(uniqueId, item.getId());
                    }
                    return ItemListResponseDto.from(item, image, nickname, wishStatus);
                })
                .toList();
    }

    //내일 나눔 마감인 아이템 조회
    public List<ItemListResponseDto> getDeadlineItems(String uniqueId) {
        List<Item> items = itemRepository.findAllByDeadline();

        return items.stream()
                .map(item -> {
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    boolean wishStatus = false;
                    if (uniqueId != null && !uniqueId.isEmpty()) {
                        wishStatus = wishRepository.existsByUniqueIdAndItemId(uniqueId, item.getId());
                    }
                    String nickname = getUserNicknameById(item.getUserId());
                    return ItemListResponseDto.from(item, image, nickname, wishStatus);
                })
                .toList();
    }
}

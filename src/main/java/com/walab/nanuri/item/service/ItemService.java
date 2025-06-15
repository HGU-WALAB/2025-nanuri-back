package com.walab.nanuri.item.service;

import com.walab.nanuri.chat.service.ChatRoomService;
import com.walab.nanuri.commons.util.ItemCategory;
import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.history.entity.History;
import com.walab.nanuri.history.repository.HistoryRepository;
import com.walab.nanuri.image.dto.response.ImageResponseDto;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.walab.nanuri.commons.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final HistoryRepository historyRepository;
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
    public List<ItemListResponseDto> getAllItems(String uniqueId, String category, String sort) {
        List<Item> items;

        if(category.isEmpty()){ // 카테고리 선택 안함
            switch (sort) {
                case "latest":
                    items = itemRepository.findAllOrderedByLatest();
                    break;
                case "oldest" :
                    items = itemRepository.findAllOrderedByOldest();
                    break;
                case "viewCount":
                    items = itemRepository.findAllByViewCountOrdered();
                    break;
                case "wishCount":
                    items = itemRepository.findAllByWishCountOrdered();
                    break;
                case "deadline":
                    items = itemRepository.findAllByDeadlineOrdered();
                    break;
                default:
                    throw new CustomException(INVALID_SORT_OPTION);
            }
        }
        else { // 카테고리 선택한 후 정렬
            switch (sort) {
                case "latest":
                    items = itemRepository.findAllByCategoryOrderedLatest(ItemCategory.valueOf(category));
                    break;
                case "oldest":
                    items = itemRepository.findAllByCategoryOrderedOldest(ItemCategory.valueOf(category));
                    break;
                case "viewCount":
                    items = itemRepository.findAllByCategoryOrderedByViewCount(ItemCategory.valueOf(category));
                    break;
                case "wishCount":
                    items = itemRepository.findAllByCategoryOrderedByWishCount(ItemCategory.valueOf(category));
                    break;
                case "deadline":
                    items = itemRepository.findAllByCategoryOrderedByDeadline(ItemCategory.valueOf(category));
                    break;
                default:
                    throw new CustomException(INVALID_SORT_OPTION);
            }
        }

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
                    // 마감기한 지난 아이템이라면 -> shareStatus를 EXPIRED로 변경
                    ShareStatus shareStatus = item.getShareStatus();
                    if (item.getDeadline() != null && item.getDeadline().isBefore(LocalDateTime.now())
                            && !shareStatus.equals(ShareStatus.EXPIRED)) {
                        shareStatus = ShareStatus.EXPIRED;
                    }
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    String nickname = getUserNicknameById(item.getUserId());
                    boolean wishStatus = wishItemIds.contains(item.getId());

                    return ItemListResponseDto.from(item, image, nickname, wishStatus, shareStatus);
                })
                .collect(Collectors.toList());
    }

    //Item 단건 조회
    @Transactional
    public ItemResponseDto getItemById(String uniqueId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));
        List<Image> images = new ArrayList<>(imageRepository.findByItemIdOrderByIdAsc(itemId));

        List<ImageResponseDto.ImageReadResponse> imageReadResponses = ImageResponseDto.ImageReadResponse.fromList(images);

        String nickname = getUserNicknameById(item.getUserId());
        boolean isOwner = item.getUserId().equals(uniqueId);
        boolean wishStatus = wishRepository.existsByUniqueIdAndItemId(uniqueId, itemId);
        ShareStatus shareStatus = item.getShareStatus();
        if (item.getDeadline() != null && item.getDeadline().isBefore(LocalDateTime.now())
                && !shareStatus.equals(ShareStatus.EXPIRED)) {
            shareStatus = ShareStatus.EXPIRED;
        }
        item.addViewCount(); // 조회수 증가
        return ItemResponseDto.from(item, imageReadResponses, isOwner, nickname, wishStatus, shareStatus);
    }

    //다른 User의 Item 전체 조회
    public List<ItemListResponseDto> getItemsByUserNickname(String nickname) {
        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
        List<Item> items = itemRepository.findAllByUserId(user.getUniqueId());

        return items.stream()
                .map(item -> {
                    ShareStatus shareStatus = item.getShareStatus();
                    if (item.getDeadline() != null && item.getDeadline().isBefore(LocalDateTime.now())
                            && !shareStatus.equals(ShareStatus.EXPIRED)) {
                        shareStatus = ShareStatus.EXPIRED;
                    }
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    boolean wishStatus = wishRepository.existsByUniqueIdAndItemId(user.getUniqueId(), item.getId());
                    return ItemListResponseDto.from(item, image, nickname, wishStatus, shareStatus);
                })
                .collect(Collectors.toList());
    }

    // 나눔물품 거래완료
    @Transactional
    public void completeItem(String uniqueId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));
        if (Objects.equals(item.getUserId(), uniqueId)) {
            item.setShareStatus(ShareStatus.COMPLETED);
            List<History> completeComplete = historyRepository.findByItemId(item.getId());
            completeComplete.forEach(History::markConfirmed);
        } else {
            throw new CustomException(USER_NOT_FOUND);
        }
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
                    ShareStatus shareStatus = item.getShareStatus();
                    if (item.getDeadline() != null && item.getDeadline().isBefore(LocalDateTime.now())
                            && !shareStatus.equals(ShareStatus.EXPIRED)) {
                        shareStatus = ShareStatus.EXPIRED;
                    }
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    String nickname = getUserNicknameById(item.getUserId());
                    boolean wishStatus = wishRepository.existsByUniqueIdAndItemId(uniqueId, item.getId());
                    return ItemListResponseDto.from(item, image, nickname, wishStatus, shareStatus);
                })
                .collect(Collectors.toList());
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
//            chatRoomService.deleteChatRoomsByItemId(itemId);
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

    //keyword로 아이템 검색
    public List<ItemListResponseDto> getSearchItems(String uniqueId, String keyword, String category) {
        List<Item> items = category.isEmpty() ?
                itemRepository.searchByKeyword(keyword) : itemRepository.searchByKeywordAndCategory(keyword, ItemCategory.valueOf(category));

        return items.stream()
                .map(item -> {
                    ShareStatus shareStatus = item.getShareStatus();
                    if (item.getDeadline() != null && item.getDeadline().isBefore(LocalDateTime.now())
                            && !shareStatus.equals(ShareStatus.EXPIRED)) {
                        shareStatus = ShareStatus.EXPIRED;
                    }
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    boolean wishStatus = false;
                    if (uniqueId != null && !uniqueId.isEmpty()) {
                        wishStatus = wishRepository.existsByUniqueIdAndItemId(uniqueId, item.getId());
                    }
                    String nickname = getUserNicknameById(item.getUserId());
                    return ItemListResponseDto.from(item, image, nickname, wishStatus, shareStatus);
                })
                .collect(Collectors.toList());
    }

    //내일 나눔 마감인 아이템 조회
    public List<ItemListResponseDto> getDeadlineItems(String uniqueId) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Item> items = itemRepository.findItemsDueTomorrow(Date.valueOf(tomorrow));

        return items.stream()
                .map(item -> {
                    ShareStatus shareStatus = item.getShareStatus();
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    boolean wishStatus = false;
                    if (uniqueId != null && !uniqueId.isEmpty()) {
                        wishStatus = wishRepository.existsByUniqueIdAndItemId(uniqueId, item.getId());
                    }
                    String nickname = getUserNicknameById(item.getUserId());
                    return ItemListResponseDto.from(item, image, nickname, wishStatus, shareStatus);
                })
                .collect(Collectors.toList());
    }
}

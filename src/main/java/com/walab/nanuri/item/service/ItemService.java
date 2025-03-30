package com.walab.nanuri.item.service;

import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.image.entity.Image;
import com.walab.nanuri.image.repository.ImageRepository;
import com.walab.nanuri.image.service.ImageService;
import com.walab.nanuri.item.dto.request.ItemRequestDto;
import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.item.dto.response.ItemResponseDto;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.walab.nanuri.commons.exception.ErrorCode.ITEM_NOT_FOUND;
import static com.walab.nanuri.commons.exception.ErrorCode.VALID_ITEM;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    //Item 추가
    @Transactional
    public Long createItem(String uniqueId, ItemRequestDto itemDto) {
        Item item = Item.toEntity(uniqueId, itemDto);
        itemRepository.save(item);

        return item.getId();
    }

    //Item 전체 조회(일반 전체 조회, 카테고리 선택 후 전체 조회)
    public List<ItemListResponseDto> getAllItems(String category) {
        List<Item> items = category.isEmpty() ?
                itemRepository.findAll() : itemRepository.findAllByCategory(category);

        return items.stream()
                .map(item -> {
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    return ItemListResponseDto.from(item, image);
                })
                .toList();
    }
    //나눔 중인 나의 Item 조회

    //Item 단건 조회
    public ItemResponseDto getItemById(String uniqueId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(RuntimeException::new);
        List<String> imageUrls = imageRepository.findByItemIdOrderByIdAsc(itemId).stream()
                .map(Image::getFileUrl)
                .collect(Collectors.toList());

        boolean isOwner = item.getUserId().equals(uniqueId);
        return ItemResponseDto.from(item, imageUrls, isOwner);
    }

    //
    public List<ItemListResponseDto> getOngoingMyItems(String uniqueId, boolean done) {
        List<Item> items = itemRepository.findAllByUserIdAndIsFinished(uniqueId, done);

        return items.stream()
                .map(item -> {
                    String image = imageRepository.findTopByItemIdOrderByIdAsc(item.getId())
                            .getFileUrl();
                    return ItemListResponseDto.from(item, image);
                })
                .toList();
    }


    //Item 수정
    @Transactional
    public void updateItem(String uniqueId, Long updateId, ItemRequestDto itemDto) {
        Item findItem = itemRepository.findById(updateId).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));

        if(findItem.getUserId().equals(uniqueId)) { // 아이템 주인이 맞을 경우
            findItem.update(itemDto.getTitle(), itemDto.getDescription(), itemDto.getPlace(), itemDto.getCategory());
        } else {
            throw new CustomException(VALID_ITEM);
        }
    }


    //Item 삭제하기
    @Transactional
    public void deleteItem(String uniqueId, Long itemId) {
        Item findItem = itemRepository.findById(itemId).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));
        if(findItem.getUserId().equals(uniqueId)) { // 아이템 주인이 맞을 경우
            imageService.deleteImages(itemId);
            itemRepository.delete(findItem);
        } else {
            throw new CustomException(VALID_ITEM);
        }
    }

}

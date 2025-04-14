package com.walab.nanuri.user.service;

import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.commons.util.Category;
import com.walab.nanuri.commons.util.ShareStatus;
import com.walab.nanuri.image.repository.ImageRepository;
import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import com.walab.nanuri.security.util.JwtUtil;
import com.walab.nanuri.user.dto.response.OtherUserInfoResponseDto;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import com.walab.nanuri.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.walab.nanuri.commons.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final WishRepository wishRepository;
    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    //유저 정보 가져 오기
    public User getUser(String uniqueId) {
        return userRepository.findById(uniqueId)
                        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    //유저 정보 수정
    public void editUserInfo(String nickname, String mbti, List<Category> interestCategory, String introduction) {
        String uniqueId = JwtUtil.getUserUniqueId();
        User user = userRepository.findById(uniqueId).orElseThrow(()->new CustomException(USER_NOT_FOUND));
        user.editUserDetails(nickname, mbti, interestCategory, introduction);
        userRepository.save(user);
    }

    // 유저 탈퇴
    public void deleteUser(String uniqueId) {
        wishRepository.deleteAll(wishRepository.findAllByUniqueId(uniqueId));
        User user = userRepository.findById(uniqueId).orElseThrow(()->new CustomException(USER_NOT_FOUND));
        userRepository.delete(user);
    }

    // 유저 닉네임 중복 체크
    public boolean checkNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }
  
    //다른 유저 마이페이지 조회
    public OtherUserInfoResponseDto getOtherUserInfo(String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        //나눔중 아이템 목록
        List<Item> sharingItems = itemRepository.findAllByUserIdAndShareStatus(user.getUniqueId(), ShareStatus.NONE);
        List<ItemListResponseDto> sharingItemList = sharingItems.stream()
                .map(item -> {
                        String imageUrl = imageRepository.findTopByItemIdOrderByIdAsc(item.getId()).getFileUrl();
                        return ItemListResponseDto.from(item, imageUrl, nickname);
                })
                .toList();

        //나눔 완료 아이템 목록
        List<Item> completedItems = itemRepository.findAllByUserIdAndShareStatus(user.getUniqueId(), ShareStatus.COMPLETED);
        List<ItemListResponseDto> completedItemList = completedItems.stream()
                .map(item -> {
                    String imageUrl = imageRepository.findTopByItemIdOrderByIdAsc(item.getId()).getFileUrl();
                    return ItemListResponseDto.from(item, imageUrl, nickname);
                })
                .toList();

        return OtherUserInfoResponseDto.builder()
                .nickname(user.getNickname())
                .mbti(user.getMbti())
                .introduction(user.getIntroduction())
                .sharingItemList(sharingItemList)
                .completedItemList(completedItemList)
                .build();
    }
}
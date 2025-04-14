package com.walab.nanuri.user.dto.response;

import com.walab.nanuri.item.dto.response.ItemListResponseDto;
import com.walab.nanuri.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OtherUserInfoResponseDto {
    private String nickname;
    private String mbti;
    private String introduction;
    private List<ItemListResponseDto> sharingItemList;
    private List<ItemListResponseDto> completedItemList;

    public static OtherUserInfoResponseDto from(User user, List<ItemListResponseDto> sharingItemList, List<ItemListResponseDto> completedItemList) {
        return OtherUserInfoResponseDto.builder()
                .nickname(user.getNickname())
                .mbti(user.getMbti())
                .introduction(user.getIntroduction())
                .sharingItemList(sharingItemList)
                .completedItemList(completedItemList)
                .build();
    }
}

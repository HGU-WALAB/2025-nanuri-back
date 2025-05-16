package com.walab.nanuri.user.dto.response;

import com.walab.nanuri.commons.util.ItemCategory;
import com.walab.nanuri.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserResponseDto {
    private String uniqueId;
    private String name;
    private String department;
    private String nickname;
    private String mbti;
    private List<String> interestItemCategory;
    private String introduction;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .uniqueId(user.getUniqueId())
                .name(user.getName())
                .department(user.getDepartment())
                .nickname(user.getNickname())
                .mbti(user.getMbti())
                .interestItemCategory(user.getInterestItemCategory().stream()
                        .map(ItemCategory::getKoreanName)
                        .toList())
                .introduction(user.getIntroduction())
                .build();
    }
}

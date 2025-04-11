package com.walab.nanuri.user.dto.response;

import com.walab.nanuri.commons.util.Category;
import com.walab.nanuri.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OtherUserResponseDto {
    private String nickname;
    private List<Category> interestCategory;
    private String mbti;
    private String introduction;

    public static OtherUserResponseDto from(User user) {
        return OtherUserResponseDto.builder()
                .nickname(user.getNickname())
                .interestCategory(user.getInterestCategory())
                .mbti(user.getMbti())
                .introduction(user.getIntroduction())
                .build();
    }
}

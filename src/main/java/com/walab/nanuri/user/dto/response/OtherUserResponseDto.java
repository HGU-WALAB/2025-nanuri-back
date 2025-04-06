package com.walab.nanuri.user.dto.response;

import com.walab.nanuri.commons.util.Tag;
import com.walab.nanuri.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OtherUserResponseDto {
    private String nickname;
    private List<Tag> interestTag;
    private String mbti;
    private String hobby;
    private String introduction;

    public static OtherUserResponseDto from(User user) {
        return OtherUserResponseDto.builder()
                .nickname(user.getNickname())
                .interestTag(user.getInterestTag())
                .mbti(user.getMbti())
                .hobby(user.getHobby())
                .introduction(user.getIntroduction())
                .build();
    }
}

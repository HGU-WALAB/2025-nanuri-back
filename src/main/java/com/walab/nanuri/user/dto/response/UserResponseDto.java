package com.walab.nanuri.user.dto.response;

import com.walab.nanuri.commons.util.Tag;
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
    private List<Tag> interestTag;
    private String hobby;
    private String introduction;

    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .uniqueId(user.getUniqueId())
                .name(user.getName())
                .department(user.getDepartment())
                .nickname(user.getNickname())
                .mbti(user.getMbti())
                .interestTag(user.getInterestTag())
                .hobby(user.getHobby())
                .introduction(user.getIntroduction())
                .build();
    }
}

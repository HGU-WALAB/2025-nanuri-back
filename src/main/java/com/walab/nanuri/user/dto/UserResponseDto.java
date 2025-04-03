package com.walab.nanuri.user.dto;

import com.walab.nanuri.commons.util.Tag;
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

    public static UserResponseDto from(UserDto dto) {
        return UserResponseDto.builder()
                .uniqueId(dto.getUniqueId())
                .name(dto.getName())
                .department(dto.getDepartment())
                .nickname(dto.getNickname())
                .mbti(dto.getMbti())
                .interestTag(dto.getInterestTag())
                .hobby(dto.getHobby())
                .introduction(dto.getIntroduction())
                .build();
    }
}

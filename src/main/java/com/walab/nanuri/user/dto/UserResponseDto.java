package com.walab.nanuri.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private String uniqueId;
    private String name;
    private String department;
    private String nickname;

    public static UserResponseDto from(UserDto dto) {
        return UserResponseDto.builder()
                .uniqueId(dto.getUniqueId())
                .name(dto.getName())
                .department(dto.getDepartment())
                .nickname(dto.getNickname())
                .build();
    }
}

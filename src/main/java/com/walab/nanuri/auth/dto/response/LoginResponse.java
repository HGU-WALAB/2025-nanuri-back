package com.walab.nanuri.auth.dto.response;

import com.walab.nanuri.auth.dto.AuthDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String token;
    private String userId;
    private String userName;
    private String department;
    private String nickname;

    public static LoginResponse from(AuthDto authDto) {
        return LoginResponse.builder()
                .token(authDto.getToken())
                .userId(authDto.getUniqueId())
                .userName(authDto.getName())
                .department(authDto.getDepartment())
                .nickname(authDto.getNickname())
                .build();
    }
}
package com.walab.nanuri.auth.dto.response;

import com.walab.nanuri.auth.dto.AuthDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String token;
    private String nickname;
    private Boolean isFirstUser;

    public static LoginResponse from(AuthDto authDto) {
        return LoginResponse.builder()
                .token(authDto.getToken())
                .nickname(authDto.getNickname())
                .build();
    }
}
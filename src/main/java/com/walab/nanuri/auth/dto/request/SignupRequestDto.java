package com.walab.nanuri.auth.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class SignupRequestDto {
    private String uniqueId;
    private String nickname;
    private String mbti;
    private List<String> interestTag;
    private String hobby;
    private String introduction;
}

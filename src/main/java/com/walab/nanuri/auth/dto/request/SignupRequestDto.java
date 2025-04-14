package com.walab.nanuri.auth.dto.request;

import com.walab.nanuri.commons.util.Category;
import lombok.Getter;

import java.util.List;

@Getter
public class SignupRequestDto {
    private String uniqueId;
    private String nickname;
    private String mbti;
    private List<Category> interestCategory;
    private String hobby;
    private String introduction;
}

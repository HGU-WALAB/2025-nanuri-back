package com.walab.nanuri.user.dto.request;

import com.walab.nanuri.commons.util.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EditUserInfoDto {
    private String nickname;
    private String mbti;
    private List<Category> interestCategory;
    private String hobby;
    private String introduction;
}

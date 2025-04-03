package com.walab.nanuri.user.dto;

import com.walab.nanuri.commons.util.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EditUserInfoDto {
    private String nickname;
    private String mbti;
    private List<Tag> interestTag;
    private String hobby;
    private String introduction;
}

package com.walab.nanuri.user.dto.request;

import com.walab.nanuri.commons.util.ItemCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EditUserInfoDto {
    private String nickname;
    private String mbti;
    private List<ItemCategory> interestItemCategory;
    private String introduction;
}

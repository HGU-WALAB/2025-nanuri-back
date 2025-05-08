package com.walab.nanuri.want.dto.request;

import com.walab.nanuri.commons.util.EmotionType;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class WantPostEmotionRequestDto {
    private Map<EmotionType, Boolean> emotions;
}

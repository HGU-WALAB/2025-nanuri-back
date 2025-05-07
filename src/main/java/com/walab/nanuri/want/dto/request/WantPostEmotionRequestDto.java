package com.walab.nanuri.want.dto.request;

import com.walab.nanuri.commons.util.EmotionType;
import lombok.Getter;

import java.util.List;

@Getter
public class WantPostEmotionRequestDto {
    private List<EmotionType> emotionTypes;
}

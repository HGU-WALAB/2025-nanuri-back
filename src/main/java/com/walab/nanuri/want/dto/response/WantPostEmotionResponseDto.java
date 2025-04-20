package com.walab.nanuri.want.dto.response;

import com.walab.nanuri.commons.util.EmotionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WantPostEmotionResponseDto {
    private EmotionType emotionType;
    private Long count;
    private boolean isClicked;
}

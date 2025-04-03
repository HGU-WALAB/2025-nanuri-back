package com.walab.nanuri.want.dto.response;

import com.walab.nanuri.want.entity.WantPost;
import lombok.*;

@Getter
@Builder
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class WantPostFormalResponseDto {
    private Long id;
    private String receiverNickName;
    private String title;
    private String description;

    public static WantPostFormalResponseDto from(WantPost wantPost, String receiverNickName) {
        return WantPostFormalResponseDto.builder()
                .id(wantPost.getId())
                .receiverNickName(receiverNickName)
                .title(wantPost.getTitle())
                .description(wantPost.getDescription())
                .build();
    }
}

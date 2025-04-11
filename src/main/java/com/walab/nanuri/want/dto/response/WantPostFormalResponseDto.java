package com.walab.nanuri.want.dto.response;

import com.walab.nanuri.want.entity.WantPost;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class WantPostFormalResponseDto {
    private Long id;
    private String receiverNickName;
    private String title;
    private String createdTime;
    private String description;
    private Boolean isOwner;

    public static WantPostFormalResponseDto from(WantPost wantPost, String receiverNickName, Boolean isOwner) {
        return WantPostFormalResponseDto.builder()
                .id(wantPost.getId())
                .receiverNickName(receiverNickName)
                .title(wantPost.getTitle())
                .createdTime(wantPost.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .description(wantPost.getDescription())
                .isOwner(isOwner)
                .build();
    }
}

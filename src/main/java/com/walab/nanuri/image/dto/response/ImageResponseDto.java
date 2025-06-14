package com.walab.nanuri.image.dto.response;

import com.walab.nanuri.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

public class ImageResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageReadResponse{
        private Long imageId;
        private String fileUrl;

        public static List<ImageReadResponse> fromList(List<Image> images) {
            return images.stream()
                    .map(l -> ImageReadResponse.builder()
                            .imageId(l.getId())
                            .fileUrl(l.getFileUrl())
                            .build())
                    .collect(Collectors.toList());
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageUrlReadResponseDto {
        private String fileUrl;

        public static ImageUrlReadResponseDto createDefaultDto(Image image) {
            return ImageUrlReadResponseDto.builder()
                    .fileUrl(image.getFileUrl())
                    .build();
        }

        public static List<ImageUrlReadResponseDto> fromList(List<Image> images) {
            return images.stream()
                    .map(l -> ImageUrlReadResponseDto.builder()
                            .fileUrl(l.getFileUrl())
                            .build())
                    .collect(Collectors.toList());
        }
    }
}

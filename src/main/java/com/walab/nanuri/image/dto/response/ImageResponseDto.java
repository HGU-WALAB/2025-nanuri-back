package com.walab.nanuri.image.dto.response;

import com.walab.nanuri.image.common.ImageExtension;
import com.walab.nanuri.image.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ImageResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImageReadResponse{
        private Long id;
        private String filePath;
        private String fileName;
        private ImageExtension fileType;
        private Long fileSize;
        private LocalDateTime regDate;

        public static ImageReadResponse createDefaultDto(Image request) {
            return ImageReadResponse.builder()
                    .fileName(request.getFileName())
                    .fileSize(request.getFileSize())
                    .fileType(request.getFileType())
                    .regDate(request.getRegDate())
                    .filePath(request.getFilePath())
                    .build();
        }

        public static List<ImageReadResponse> fromList(List<Image> images) {
            return images.stream()
                    .map(l -> ImageReadResponse.builder()
                            .fileName(l.getFileName())
                            .fileSize(l.getFileSize())
                            .fileType(l.getFileType())
                            .regDate(l.getRegDate())
                            .filePath(l.getFilePath())
                            .build())
                    .collect(Collectors.toList());
        }

    }
}

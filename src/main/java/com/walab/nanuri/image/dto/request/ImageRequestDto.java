package com.walab.nanuri.image.dto.request;

import com.walab.nanuri.image.common.ImageExtension;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ImageRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class ImageCreateDto {
        private String fileName;
        private String filePath;
        private Long fileSize;
        private ImageExtension fileType;
    }
}

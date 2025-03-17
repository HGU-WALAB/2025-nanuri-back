package com.walab.nanuri.image.service;


import com.walab.nanuri.commons.exception.FileCommonException;
import com.walab.nanuri.commons.exception.FileExceptionCode;
import com.walab.nanuri.image.common.ImageExtension;
import com.walab.nanuri.image.entity.Image;
import com.walab.nanuri.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void uploadImage(List<MultipartFile> files) throws IOException {
        if (files.size() > 5){
            throw new FileCommonException(FileExceptionCode.FILE_COUNT_UPPER);
        }

        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new FileCommonException(FileExceptionCode.FILE_UPLOAD_FAILED);
            }

            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toUpperCase();
            log.info("extension: " + extension);
            if(!ImageExtension.isValidExtension(extension)){
                throw new FileCommonException(FileExceptionCode.UNSUPPORTED_FILE_TYPE);
            }

            Path directoryPath = Paths.get(uploadDir);
            Files.createDirectories(directoryPath); // 내부적으로 존재 여부 체크,

            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            Path path = Paths.get(uploadDir, uniqueFilename);

            Files.write(path, file.getBytes());

            Image image = Image.builder()
                    .fileName(originalFilename)
                    .filePath(uniqueFilename)
                    .fileType(ImageExtension.valueOf(extension))
                    .fileSize(file.getSize())
                    .build();

            imageRepository.save(image);
        }
    }

    public ResponseEntity<Resource> viewImages(Long imageId) throws IOException {
        log.info("imageIds: " + imageId);

        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new FileCommonException(FileExceptionCode.FILE_NOT_FOUND));

        Path path = Paths.get(uploadDir, image.getFilePath());
        try {
            Resource resource = new UrlResource(path.toUri());
            log.info("resource: " + resource);

            if (!resource.exists() || !resource.isReadable()) {
                throw new FileCommonException(FileExceptionCode.FILE_NOT_FOUND);
            }

            String encodedFileName = URLEncoder.encode(image.getFileName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");
            log.info("encodedFileName: " + ImageExtension.getMimeType(image.getFileType()));

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(ImageExtension.getMimeType(image.getFileType())))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename*=UTF-8''" + encodedFileName)
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new FileCommonException(FileExceptionCode.FILE_PATH_INVALID);
        }
    }
}

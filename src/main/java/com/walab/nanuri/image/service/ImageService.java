package com.walab.nanuri.image.service;


import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.commons.exception.file.FileCommonException;
import com.walab.nanuri.commons.exception.file.FileExceptionCode;
import com.walab.nanuri.image.common.ImageExtension;
import com.walab.nanuri.image.entity.Image;
import com.walab.nanuri.image.repository.ImageRepository;
import com.walab.nanuri.item.entity.Item;
import com.walab.nanuri.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.walab.nanuri.commons.exception.ErrorCode.ITEM_NOT_FOUND;
import static com.walab.nanuri.commons.exception.ErrorCode.ITEM_OWNER_MISMATCH;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;
    private final ItemRepository itemRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${image.url}")
    private String imageUrl;

    @Value("${base.url}")
    private String baseUrl;

    public void uploadImage(List<MultipartFile> files, Long itemId) throws IOException {
        if (files.size() > 5){
            throw new FileCommonException(FileExceptionCode.FILE_COUNT_UPPER);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));

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
            Files.createDirectories(directoryPath);

            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            Path path = Paths.get(uploadDir, uniqueFilename);

            Files.write(path, file.getBytes());

            Image image = Image.builder()
                    .fileName(originalFilename)
                    .filePath(uniqueFilename)
                    .fileUrl(baseUrl + imageUrl + uniqueFilename)
                    .fileType(ImageExtension.valueOf(extension))
                    .fileSize(file.getSize())
                    .item(item)
                    .build();

            imageRepository.save(image);
        }
    }

    private void deleteAllImages(List<Image> images) {
        for (Image image : images) {
            Path path = Paths.get(uploadDir, image.getFilePath());
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                log.warn("이미지 삭제 실패: {}", path);
            }
            imageRepository.delete(image);
        }
    }

    public void deleteImageInItemId(String uniqueId, Long itemId, List<Long> imageIds) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new CustomException(ITEM_NOT_FOUND));
        if(Objects.equals(item.getUserId(), uniqueId)){
            List<Image> images = imageRepository.findByItemIdOrderByIdAsc(itemId);
            List<Image> deleteImages = images.stream()
                    .filter(image -> imageIds.contains(image.getId()))
                    .collect(Collectors.toList());
            if (deleteImages.isEmpty()) {
                return;
            }

            deleteAllImages(deleteImages);
        } else {
            throw new CustomException(ITEM_OWNER_MISMATCH);
        }
    }

    public ResponseEntity<Resource> viewImages(Long itemId) throws IOException {
        Long imageId = imageRepository.findTopByItemIdOrderByIdAsc(itemId).getId();

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

    public void deleteImages(Long itemId) {
        List<Image> images = imageRepository.findByItemIdOrderByIdAsc(itemId);
        for (Image image : images) {
            try {
                Path path = Paths.get(uploadDir, image.getFilePath());
                Files.deleteIfExists(path);
            } catch (IOException e) {
                log.error("Failed to delete file: " + image.getFilePath(), e);
                throw new FileCommonException(FileExceptionCode.FILE_DELETE_FAILED);
            }
            imageRepository.delete(image);
        }

    }
}

package com.walab.nanuri.image.controller;

import com.walab.nanuri.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/image")
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/{itemId}")
    public void upload(@RequestParam("files") List<MultipartFile> files, @PathVariable Long itemId) throws IOException {
        imageService.uploadImage(files, itemId);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteImagesInItem(@AuthenticationPrincipal String uniqueId, @PathVariable Long itemId, @RequestBody List<Long> imageIds){
        imageService.deleteImageInItemId(uniqueId, itemId, imageIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> viewImages(@PathVariable Long id) throws IOException {
        return imageService.viewImages(id);
    }
}

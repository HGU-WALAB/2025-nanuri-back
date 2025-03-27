package com.walab.nanuri.wish.controller;

import com.walab.nanuri.wish.dto.request.WishRequestDto;
import com.walab.nanuri.wish.dto.response.WishResponseDto;
import com.walab.nanuri.wish.service.WishService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/wish")
@RequiredArgsConstructor
public class WishController {

    private final WishService wishService;

    @PostMapping
    public ResponseEntity<Void> createWish(
            @AuthenticationPrincipal String uniqueId,
            @RequestBody WishRequestDto.WishCreateRequestDto requestDto) {
        wishService.createWish(uniqueId, requestDto.getItemId());
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWish(
            @AuthenticationPrincipal String uniqueId,
            @RequestBody WishRequestDto.WishDeleteRequestDto requestDto) {
        wishService.deleteWish(uniqueId, requestDto.getItemId());
        return ResponseEntity.ok().body(null);
    }

    @GetMapping
    public ResponseEntity<List<WishResponseDto>> getWishList(@AuthenticationPrincipal String uniqueId) {
        List<WishResponseDto> wishList = wishService.getWishList(uniqueId);
        return ResponseEntity.ok().body(wishList);
    }
}

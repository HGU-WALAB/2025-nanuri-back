package com.walab.nanuri.want.controller;

import com.walab.nanuri.want.dto.request.WantPostRequestDto;
import com.walab.nanuri.want.dto.response.WantPostFormalResponseDto;
import com.walab.nanuri.want.service.WantPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/want")
public class WantPostController {
    private final WantPostService wantPostService;

    @PostMapping("")
    public ResponseEntity<String> createPost(@AuthenticationPrincipal String uniqueId, @RequestBody WantPostRequestDto wantPost) {
        wantPostService.createPost(wantPost, uniqueId);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/{postId}/select")
    public ResponseEntity<String> selectReceiver(@AuthenticationPrincipal String uniqueId, @PathVariable Long postId) {
        wantPostService.selectPost(uniqueId, postId);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<WantPostFormalResponseDto> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok().body(wantPostService.findById(postId));
    }

    @GetMapping("/posts")
    public ResponseEntity<List<WantPostFormalResponseDto>> getPosts() {
        return ResponseEntity.ok().body(wantPostService.findAll());
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<String> updatePost(@AuthenticationPrincipal String uniqueId, @PathVariable Long postId, @RequestBody WantPostRequestDto wantPost) {
        wantPostService.updatePost(uniqueId, postId, wantPost);
        return ResponseEntity.ok().body(null);
    }

    @PatchMapping("/{postId}/done")
    public ResponseEntity<String> isFinished(@AuthenticationPrincipal String uniqueId, @PathVariable Long postId, @RequestParam Boolean isFinished) {
        System.out.println("uniqueId = " + uniqueId);
        wantPostService.fetchFinish(uniqueId, postId, isFinished);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@AuthenticationPrincipal String uniqueId, @PathVariable Long postId) {
        wantPostService.deletePost(uniqueId, postId);
        return ResponseEntity.ok().body(null);
    }
}

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
@RequestMapping("/api/want")
public class WantPostController {
    private final WantPostService wantPostService;

    //WantPost 등록
    @PostMapping
    public ResponseEntity<String> createPost(@AuthenticationPrincipal String uniqueId, @RequestBody WantPostRequestDto wantPost) {
        wantPostService.createPost(wantPost, uniqueId);
        return ResponseEntity.ok().body(null);
    }

    //나눔자가 WantPost글에 나눔 해준다는 신청
    @PostMapping("/{postId}/select")
    public ResponseEntity<String> selectReceiver(@AuthenticationPrincipal String uniqueId, @PathVariable Long postId) {
        wantPostService.selectPost(uniqueId, postId);
        return ResponseEntity.ok().body(null);
    }

    // WantPost 글 단건 조회
    @GetMapping("/{postId}")
    public ResponseEntity<WantPostFormalResponseDto> getPostById(@AuthenticationPrincipal String uniqueId, @PathVariable Long postId) {
        return ResponseEntity.ok().body(wantPostService.getPostById(uniqueId, postId));
    }

    //WantPost 글 전체 조회
    @GetMapping("/posts")
    public ResponseEntity<List<WantPostFormalResponseDto>> getAllPosts(@AuthenticationPrincipal String uniqueId) {
        return ResponseEntity.ok().body(wantPostService.getAllPosts(uniqueId));
    }

    // WantPost 글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<String> updatePost(@AuthenticationPrincipal String uniqueId, @PathVariable Long postId, @RequestBody WantPostRequestDto wantPost) {
        wantPostService.updatePost(uniqueId, postId, wantPost);
        return ResponseEntity.ok().body(null);
    }

    // 나눔 받기 완료
    @PatchMapping("/{postId}/done")
    public ResponseEntity<String> isFinished(@AuthenticationPrincipal String uniqueId, @PathVariable Long postId, @RequestParam Boolean isFinished) {
        System.out.println("uniqueId = " + uniqueId);
        wantPostService.isFinished(uniqueId, postId, isFinished);
        return ResponseEntity.ok().body(null);
    }

    // WantPost 글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@AuthenticationPrincipal String uniqueId, @PathVariable Long postId) {
        wantPostService.deletePost(uniqueId, postId);
        return ResponseEntity.ok().body(null);
    }
}

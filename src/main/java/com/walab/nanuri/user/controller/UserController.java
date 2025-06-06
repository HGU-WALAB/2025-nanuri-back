package com.walab.nanuri.user.controller;

import com.walab.nanuri.user.dto.request.EditUserInfoDto;
import com.walab.nanuri.user.dto.response.OtherUserInfoResponseDto;
import com.walab.nanuri.user.dto.response.UserResponseDto;
import com.walab.nanuri.user.service.UserService;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 유저 정보 가져오기
    @GetMapping
    public ResponseEntity<UserResponseDto> getProfile(@AuthenticationPrincipal String uniqueId) {
        return ResponseEntity.ok(UserResponseDto.from(userService.getUser(uniqueId)));
    }

    //회원 정보 수정
    @PatchMapping()
    public ResponseEntity<Void> editUserInfo(@RequestBody EditUserInfoDto request){
        userService.editUserInfo(request.getNickname(), request.getMbti(), request.getInterestItemCategory(),
                request.getIntroduction());
        return ResponseEntity.ok().build();
    }

    // 유저 탈퇴
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal String uniqueId, HttpServletResponse response) {
        userService.deleteUser(uniqueId);

        Cookie accessCookie = new Cookie("accessToken_handful", "");
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0); // 쿠키 삭제

        Cookie refreshCookie = new Cookie("refreshToken_handful", "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0); // 쿠키 삭제

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok().build();
    }


    // 유저 닉네임 중복 체크
    @GetMapping("/checkNickname")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestParam String nickname) {
        return ResponseEntity.ok(userService.checkNicknameDuplicate(nickname));
    }
  
    //다른 유저 마이페이지 조회
    @GetMapping("/{userNickname}")
    public ResponseEntity<OtherUserInfoResponseDto> getOtherUserInfo(@PathVariable String userNickname) {
        return ResponseEntity.ok(userService.getOtherUserInfo(userNickname));
    }

}

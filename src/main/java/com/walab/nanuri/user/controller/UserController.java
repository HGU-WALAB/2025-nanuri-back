package com.walab.nanuri.user.controller;

import com.walab.nanuri.user.dto.EditNickname;
import com.walab.nanuri.user.dto.UserDto;
import com.walab.nanuri.user.dto.UserResponseDto;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDto> getProfile(@AuthenticationPrincipal String uniqueId) {
        return ResponseEntity.ok(UserResponseDto.from(userService.getUser(uniqueId)));
    }

    //닉네임 변경
    @PatchMapping()
    public ResponseEntity<String> editNickname(@RequestBody EditNickname request){
        userService.editNickname(request.getNickname());
        return ResponseEntity.ok("Update Nickname Success");
    }

    // 유저 탈퇴
    @DeleteMapping
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal String uniqueId, HttpServletResponse response) {
        userService.deleteUser(uniqueId);

        Cookie accessCookie = new Cookie("accessToken", "");
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(false);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0); // 쿠키 삭제

        Cookie refreshCookie = new Cookie("refreshToken", "");
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0); // 쿠키 삭제

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok().build();
    }
}

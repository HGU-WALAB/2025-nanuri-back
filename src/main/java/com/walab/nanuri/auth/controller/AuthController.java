package com.walab.nanuri.auth.controller;

import com.walab.nanuri.auth.dto.request.LoginRequest;
import com.walab.nanuri.auth.dto.request.SignupRequestDto;
import com.walab.nanuri.auth.dto.response.LoginResponse;
import com.walab.nanuri.auth.service.AuthService;
import com.walab.nanuri.auth.dto.AuthDto;
import com.walab.nanuri.auth.service.HisnetLoginService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nanuri/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final HisnetLoginService hisnetLoginService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        AuthDto authDto = hisnetLoginService.callHisnetLoginApi(AuthDto.from(request));
        LoginResponse loginResponse = LoginResponse.from(authService.login(authDto));

        String accessToken_handful = loginResponse.getToken();
        String refreshToken_handful = authService.createRefreshToken(
                loginResponse.getUserId(),
                loginResponse.getUserName()
        );

        // ✅ 쿠키 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "accessToken_handful=" + accessToken_handful + "; HttpOnly; Secure; Path=/; Max-Age=7200; SameSite=Strict;");
        headers.add(HttpHeaders.SET_COOKIE, "refreshToken_handful=" + refreshToken_handful + "; HttpOnly; Secure; Path=/; Max-Age=604800; SameSite=Strict;");

        return ResponseEntity.ok()
                .headers(headers)
                .body(loginResponse);
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        authService.signUp(signupRequestDto);
        return ResponseEntity.ok().build();
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
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
}
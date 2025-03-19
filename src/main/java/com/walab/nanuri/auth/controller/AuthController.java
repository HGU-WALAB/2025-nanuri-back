package com.walab.nanuri.auth.controller;

import com.walab.nanuri.auth.dto.request.LoginRequest;
import com.walab.nanuri.auth.dto.request.SignupRequestDto;
import com.walab.nanuri.auth.dto.response.LoginResponse;
import com.walab.nanuri.auth.service.AuthService;
import com.walab.nanuri.auth.dto.AuthDto;
import com.walab.nanuri.auth.service.HisnetLoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> Login(@RequestBody LoginRequest request) {
        AuthDto authDto = hisnetLoginService.callHisnetLoginApi(AuthDto.from(request));
        LoginResponse loginResponse = LoginResponse.from(authService.login(authDto));

        String accessToken = authService.createAccessToken(
                loginResponse.getUserId(),
                loginResponse.getUserName(),
                loginResponse.getDepartment()
        );
        String refreshToken = authService.createRefreshToken(
                loginResponse.getUserId(),
                loginResponse.getUserName()
        );

        // ✅ 쿠키 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "accessToken=" + accessToken + "; HttpOnly; Path=/; Max-Age=7200; SameSite=Lax;");
        headers.add(HttpHeaders.SET_COOKIE, "refreshToken=" + refreshToken + "; HttpOnly; Path=/; Max-Age=604800; SameSite=Lax;");

        return ResponseEntity.ok()
                .headers(headers)
                .body(loginResponse);
    }

    @PostMapping("/signup")
    public void SignUp(@RequestBody SignupRequestDto signupRequestDto) {
        authService.SetUserNickname(signupRequestDto);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
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
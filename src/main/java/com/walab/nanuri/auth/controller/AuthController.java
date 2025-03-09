package com.walab.nanuri.auth.controller;

import com.walab.nanuri.auth.dto.request.LoginRequest;
import com.walab.nanuri.auth.dto.request.SignupRequestDto;
import com.walab.nanuri.auth.dto.response.LoginResponse;
import com.walab.nanuri.auth.service.AuthService;
import com.walab.nanuri.auth.dto.AuthDto;
import com.walab.nanuri.auth.service.HisnetLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nanuri/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final HisnetLoginService hisnetLoginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> Login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(LoginResponse.from(authService.login(hisnetLoginService.callHisnetLoginApi(AuthDto.from(request)))));
    }

    @PostMapping("signup")
    public void SignUp(@RequestBody SignupRequestDto signupRequestDto) {
        authService.SetUserNickname(signupRequestDto);
    }
}
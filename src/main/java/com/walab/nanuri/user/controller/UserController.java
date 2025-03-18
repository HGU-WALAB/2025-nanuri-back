package com.walab.nanuri.user.controller;

import com.walab.nanuri.security.util.JwtUtil;
import com.walab.nanuri.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    //닉네임 변경
    @PatchMapping()
    public ResponseEntity<String> editNickname(){
        //auth에서 로그인 한 hisnet 계정 정보 가져옴
    }
}

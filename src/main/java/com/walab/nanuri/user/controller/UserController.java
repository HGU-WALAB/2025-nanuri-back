package com.walab.nanuri.user.controller;

import com.walab.nanuri.user.dto.EditNickname;
import com.walab.nanuri.user.dto.UserDto;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    //닉네임 변경
    @PatchMapping()
    public ResponseEntity<String> editNickname(@RequestBody EditNickname request){
        userService.editNickname(request.getNickname());
        return ResponseEntity.ok("Update Nickname Success");
    }
}

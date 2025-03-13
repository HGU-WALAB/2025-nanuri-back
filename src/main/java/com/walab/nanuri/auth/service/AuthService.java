package com.walab.nanuri.auth.service;

import com.walab.nanuri.auth.dto.AuthDto;
import com.walab.nanuri.auth.dto.request.SignupRequestDto;
import com.walab.nanuri.commons.exception.DoNotExistException;
import com.walab.nanuri.security.util.JwtUtil;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User getLoginUser(String uniqueId) {
        return userRepository
                .findById(uniqueId)
                .orElseThrow(() -> new DoNotExistException("해당 유저가 없습니다."));
    }

    @Transactional
    public AuthDto login(AuthDto authDto) {
        Optional<User> user = userRepository.findById(authDto.getUniqueId());

        // 최초 로그인한 경우
        if (user.isEmpty()) {
            User newUser = User.from(authDto);
            userRepository.save(User.from(authDto));
            return AuthDto.builder()
                    .token(JwtUtil.createToken(newUser.getUniqueId(), newUser.getName(), newUser.getDepartment()))
                    .build();
        } else {
            user.get().update(authDto);
            return AuthDto.builder()
                    .token(JwtUtil.createToken(user.get().getUniqueId(), user.get().getName(), user.get().getDepartment()))
                    .nickname(user.get().getNickname())
                    .build();
        }
    }

    @Transactional
    public void SetUserNickname(SignupRequestDto signupRequestDto) {
        User user = userRepository.findById(signupRequestDto.getUniqueId())
                .orElseThrow(() -> new DoNotExistException("해당 유저가 없습니다."));

        user.setNickname(signupRequestDto.getNickname());
    }
}
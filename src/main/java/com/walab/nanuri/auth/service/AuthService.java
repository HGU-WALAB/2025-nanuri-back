package com.walab.nanuri.auth.service;

import com.walab.nanuri.auth.dto.AuthDto;
import com.walab.nanuri.auth.dto.request.SignupRequestDto;
import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.security.util.JwtUtil;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Optional;

import static com.walab.nanuri.commons.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    @Value("${jwt.secret_key}")
    private String SECRET_KEY;

    public User getLoginUser(String uniqueId) {
        return userRepository
                .findById(uniqueId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));
    }

    // 로그인
    @Transactional
    public AuthDto login(AuthDto authDto) {
        Optional<User> user = userRepository.findById(authDto.getUniqueId());
        User loggedInUser = user.orElseGet(() -> User.from(authDto));
        userRepository.save(loggedInUser);

        Key key = JwtUtil.getSigningKey(SECRET_KEY);

        String accessToken = JwtUtil.createToken(
                loggedInUser.getUniqueId(),
                loggedInUser.getName(),
                loggedInUser.getDepartment(),
                key
        );

        log.info("✅ Generated AccessToken: {}", accessToken);

        return AuthDto.builder()
                .token(accessToken)
                .uniqueId(loggedInUser.getUniqueId())
                .name(loggedInUser.getName())
                .department(loggedInUser.getDepartment())
                .nickname(loggedInUser.getNickname())
                .build();
    }

    // AccessToken 생성
    public String createAccessToken(String uniqueId, String name, String department) {
        Key key = JwtUtil.getSigningKey(SECRET_KEY);
        return JwtUtil.createToken(uniqueId, name, department, key);
    }

    // RefreshToken 생성
    public String createRefreshToken(String uniqueId, String name) {
        Key key = JwtUtil.getSigningKey(SECRET_KEY);
        return JwtUtil.createRefreshToken(uniqueId, name, key);
    }

    // 회원 정보 받기
    @Transactional
    public void setUserInfo(SignupRequestDto signupRequestDto) {
        User user = userRepository.findById(signupRequestDto.getUniqueId())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        user.editUserDetails(
                signupRequestDto.getNickname(),
                signupRequestDto.getMbti(),
                signupRequestDto.getInterestCategory(),
                signupRequestDto.getHobby(),
                signupRequestDto.getIntroduction()
        );
        userRepository.save(user);
    }


}
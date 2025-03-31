package com.walab.nanuri.user.service;

import com.walab.nanuri.commons.exception.CustomException;
import com.walab.nanuri.security.util.JwtUtil;
import com.walab.nanuri.user.dto.UserDto;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import com.walab.nanuri.wish.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.walab.nanuri.commons.exception.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final WishRepository wishRepository;

    //유저 정보 가져 오기
    public UserDto getUser(String uniqueId) {
        return UserDto.from(
                userRepository
                        .findById(uniqueId)
                        .orElseThrow(() -> new CustomException(USER_NOT_FOUND)));
    }

    //닉네임 수정
    public void editNickname(String newNickname) {
        String uniqueId = JwtUtil.getUserUniqueId();
        User user = userRepository.findById(uniqueId).orElseThrow(()->new CustomException(USER_NOT_FOUND));
        user.editNickname(newNickname);
        userRepository.save(user);
    }

    // 유저 탈퇴
    public void deleteUser(String uniqueId) {
        wishRepository.deleteAll(wishRepository.findAllByUniqueId(uniqueId));
        User user = userRepository.findById(uniqueId).orElseThrow(()->new CustomException(USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
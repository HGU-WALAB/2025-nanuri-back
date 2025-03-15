package com.walab.nanuri.user.service;

import com.walab.nanuri.user.dto.UserDto;
import com.walab.nanuri.user.entity.User;
import com.walab.nanuri.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto getUser(String uniqueId) {
        return UserDto.from(
                userRepository
                        .findById(uniqueId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다.")));
    }

    //유저 탈퇴
    public void deleteUser(String uniqueId) {
        User user = userRepository.findById(uniqueId).orElseThrow(()->new IllegalArgumentException("해당 사용자가 없습니다"));
        userRepository.delete(user);
    }
}
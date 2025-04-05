package com.walab.nanuri.user.repository;

import com.walab.nanuri.user.dto.UserDto;
import com.walab.nanuri.user.dto.UserResponseDto;
import com.walab.nanuri.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByNickname(String nickname);
}
package com.walab.nanuri.user.repository;

import com.walab.nanuri.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByNickname(String nickname);
}
package com.example.backend.repository;

import com.example.backend.dto.UserDto;
import com.example.backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface IUserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByPhone(String phone);
    Optional<User> findUserByEmailAndPassword(String email, String password);
    Optional<UserDto> findByEmail(String email);
}

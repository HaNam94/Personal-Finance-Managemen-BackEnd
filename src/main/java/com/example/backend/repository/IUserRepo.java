package com.example.backend.repository;

import com.example.backend.dto.UserDto;
import com.example.backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByPhone(String phone);

    Optional<User> findUserByEmailAndPassword(String email, String password);

    User findByResetToken(String token);

    //@Query(value = "select u from user u where email like :email" ,nativeQuery = true)
    Optional<UserDto> findByEmail(String email);

    @Query(value = "call delete_user(:id)", nativeQuery = true)
    void deleteUserById(@Param("id") Long id);

    @Query("SELECT u FROM User u WHERE u.isActive = true AND u.userStatus = true")
    List<User> findAllByIsActiveAndStatus();
}

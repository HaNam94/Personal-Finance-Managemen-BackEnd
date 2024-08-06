package com.example.backend.repository;

import com.example.backend.model.dto.UserDto;
import com.example.backend.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepo extends PagingAndSortingRepository<User, Long>, CrudRepository<User, Long> {
    Optional<UserDto> findUserById(Long id);
}

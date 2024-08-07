package com.example.backend.repository;

import com.example.backend.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepo extends JpaRepository<Role,Long> {
Optional<Role> findRolesByRoleName(String roleName);

}

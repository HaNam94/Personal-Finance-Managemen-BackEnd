package com.example.backend.repository;

import com.example.backend.model.entity.MyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMyCategoryRepo extends JpaRepository<MyCategory, Long> {
}

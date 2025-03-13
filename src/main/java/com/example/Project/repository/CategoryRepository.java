package com.example.Project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Project.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

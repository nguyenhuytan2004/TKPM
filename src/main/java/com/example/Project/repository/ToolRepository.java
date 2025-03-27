package com.example.Project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Project.model.Tool;

public interface ToolRepository extends JpaRepository<Tool, Integer> {
    List<Tool> findByIsActiveTrue();

    List<Tool> findByNameContainingIgnoreCase(String name);
}

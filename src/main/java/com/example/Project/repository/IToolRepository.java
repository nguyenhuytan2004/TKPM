package com.example.Project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Project.model.Tool;

public interface IToolRepository extends JpaRepository<Tool, Integer> {
    List<Tool> findByIsActiveTrue();

    List<Tool> findByNameContainingIgnoreCase(String name);

    Optional<Tool> findByName(String name);

    Optional<Tool> findByEndpoint(String endpoint);
}

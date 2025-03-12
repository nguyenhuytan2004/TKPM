package com.example.Project.repository;

import com.example.Project.model.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ToolRepository extends JpaRepository<Tool, Long> {
    List<Tool> findByIsActiveTrue();
}

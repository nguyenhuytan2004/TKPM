package com.example.Project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Project.model.Tool;
import com.example.Project.repository.ToolRepository;

@Service
public class ToolService {
    @Autowired
    private ToolRepository toolRepository;

    public List<Tool> getAllTools() {
        return toolRepository.findAll();

    }

    public List<Tool> getToolsByName(String hintText) {
        return toolRepository.findByNameContainingIgnoreCase(hintText);
    }

    public Tool getToolById(Integer toolId) {
        return toolRepository.findById(toolId).orElse(null);
    }

    public void updateTool(Tool tool) {
        toolRepository.save(tool);
    }
}

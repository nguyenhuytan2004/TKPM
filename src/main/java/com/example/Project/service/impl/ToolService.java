package com.example.Project.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Project.model.Tool;
import com.example.Project.repository.IToolRepository;
import com.example.Project.service.IToolService;

@Service
public class ToolService implements IToolService {
    @Autowired
    private IToolRepository _toolRepository;

    @Override
    public List<Tool> getAllTools() {
        return _toolRepository.findAll();

    }

    @Override
    public List<Tool> getToolsByName(String hintText) {
        return _toolRepository.findByNameContainingIgnoreCase(hintText);
    }

    @Override
    public Tool getToolById(Integer toolId) {
        return _toolRepository.findById(toolId).orElse(null);
    }

    @Override
    public void updateTool(Tool tool) {
        _toolRepository.save(tool);
    }
}

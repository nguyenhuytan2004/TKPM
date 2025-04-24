package com.example.Project.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Project.model.Category;
import com.example.Project.model.Tool;
import com.example.Project.repository.IToolRepository;
import com.example.Project.service.ICategoryService;
import com.example.Project.service.IToolService;

@Service
public class ToolService implements IToolService {
    @Autowired
    private IToolRepository _toolRepository;

    @Autowired
    private ICategoryService _categoryService;

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

    @Override
    public void deleteTool(Tool tool) {
        _toolRepository.delete(tool);
    }

    @Override
    public Map<Integer, List<Tool>> getToolsPerCategory() {
        List<Tool> allTools = _toolRepository.findAll();
        List<Category> allCategories = _categoryService.getAllCategories();
        Map<Integer, List<Tool>> toolsPerCategory = new HashMap<>();
        for (Category category : allCategories) {
            List<Tool> toolsInCategory = allTools.stream()
                    .filter(tool -> Objects.equals(tool.getCategory().getId(), category.getId()))
                    .collect(Collectors.toList());
            toolsPerCategory.put(category.getId(), toolsInCategory);
        }
        return toolsPerCategory;
    }

    @Override
    public Tool getToolByName(String name) {
        return _toolRepository.findByName(name).orElse(null);
    }

    @Override
    public boolean isPremiumToolByName(String name) {
        Tool tool = getToolByName(name);
        return tool != null && Boolean.TRUE.equals(tool.getIsPremium());
    }
}

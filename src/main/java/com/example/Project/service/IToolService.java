package com.example.Project.service;

import java.util.List;

import com.example.Project.model.Tool;

public interface IToolService {
    List<Tool> getAllTools();

    List<Tool> getToolsByName(String hintText);

    Tool getToolById(Integer toolId);

    void updateTool(Tool tool);

    void deleteTool(Tool tool);
}

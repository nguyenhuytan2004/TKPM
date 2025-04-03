package com.example.Project.service;

import com.example.Project.model.Tool;
import java.util.List;

public interface IToolService {
    List<Tool> getAllTools();

    List<Tool> getToolsByName(String hintText);

    Tool getToolById(Integer toolId);

    void updateTool(Tool tool);
}

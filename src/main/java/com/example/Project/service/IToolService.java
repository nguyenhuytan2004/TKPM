package com.example.Project.service;

import java.util.List;
import java.util.Map;

import com.example.Project.model.Tool;

public interface IToolService {
    List<Tool> getAllTools();

    List<Tool> getAllActivationTools();

    List<Tool> getToolsByName(String hintText);

    Tool getToolById(Integer toolId);

    Tool getToolByName(String name);

    Tool getToolByEndpoint(String endpoint);

    void updateTool(Tool tool);

    void deleteTool(Tool tool);

    Map<Integer, List<Tool>> getToolsPerCategory();

    boolean isPremiumToolByName(String name);
}

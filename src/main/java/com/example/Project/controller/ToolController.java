package com.example.Project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Tool;
import com.example.Project.service.ToolService;

@Controller
@RequestMapping("")
public class ToolController {
    @Autowired
    private ToolService toolService;

    public List<Tool> getAllTools() {
        return toolService.getAllTools();
    }

    public List<Tool> getToolsByName(String name) {
        return toolService.getToolsByName(name);
    }

    public Tool getToolById(int id) {
        return toolService.getToolById(id);
    }

    @RestController
    @RequestMapping("/api")
    public class ToolHandler {
        @GetMapping("/search-tool")
        public List<Tool> searchTool(@RequestParam String hintText) {
            return toolService.getToolsByName(hintText);
        }

        @PutMapping("/tool/{id}/premium")
        public boolean updateToolPremiumStatus(@PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
            Tool tool = toolService.getToolById(id);
            if (tool != null) {
                tool.setPremium(requestBody.get("isPremium"));
                toolService.updateTool(tool);
                return true;
            }
            return false;
        }

        @PutMapping("/tool/{id}/active")
        public boolean updateToolActiveStatus(@PathVariable int id, @RequestBody Map<String, Boolean> requestBody) {
            Tool tool = toolService.getToolById(id);
            if (tool != null) {
                tool.setActive(requestBody.get("isActive"));
                toolService.updateTool(tool);
                return true;
            }
            return false;
        }
    }
}

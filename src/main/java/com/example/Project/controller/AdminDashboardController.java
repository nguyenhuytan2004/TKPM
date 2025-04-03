package com.example.Project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Project.model.Category;
import com.example.Project.model.Tool;
import com.example.Project.service.ICategoryService;
import com.example.Project.service.IToolService;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    @Autowired
    private IToolService _toolService;
    @Autowired
    private ICategoryService _categoryService;

    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
        List<Tool> tools = _toolService.getAllTools();
        List<Map<String, Object>> toolList = new ArrayList<>();
        for (Tool tool : tools) {
            Map<String, Object> toolData = new HashMap<>();
            toolData.put("id", tool.getId());
            toolData.put("name", tool.getName());
            toolData.put("description", tool.getDescription());
            toolData.put("categoryName", tool.getCategory().getName());
            toolData.put("is_premium", tool.isPremium());
            toolData.put("is_active", tool.isActive());
            toolList.add(toolData);
        }

        List<Category> categories = _categoryService.getAllCategories();

        model.addAttribute("tools", toolList);
        model.addAttribute("categories", categories);

        return "admin_dashboard";
    }
}

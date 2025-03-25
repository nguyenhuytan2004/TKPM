package com.example.Project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Category;
import com.example.Project.model.Tool;
import com.example.Project.service.CategoryService;
import com.example.Project.service.ToolService;

@Controller
@RequestMapping("")
public class HomeController {
    @Autowired
    private CategoryService categoriesService;
    @Autowired
    private ToolService toolService;

    @GetMapping("")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        List<Tool> allTools = toolService.getAllTools();
        model.addAttribute("categories", allCategories);
        model.addAttribute("tools", allTools);
        return "home";
    }

    @RestController
    @RequestMapping("/api")
    public class HomeHandler {
        @GetMapping("/search-tool")
        public List<Tool> searchTool(@RequestParam String hintText) {
            return toolService.getToolsByName(hintText);
        }
    }
}

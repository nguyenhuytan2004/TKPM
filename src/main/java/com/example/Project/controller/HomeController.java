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

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class HomeController {
    @Autowired
    private CategoryService categoriesService;
    @Autowired
    private ToolService toolService;

    @GetMapping("")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        if (username != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            model.addAttribute("userId", userId);
        }

        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);

        List<Tool> allTools = toolService.getAllTools();
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

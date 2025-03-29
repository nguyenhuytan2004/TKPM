package com.example.Project.controller.development;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("development")
public class GitCheatsheetController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/git-cheatsheet")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Git Cheatsheet"); // Thêm title
        model.addAttribute("body", "git-cheatsheet"); // Load template con
        return "layout"; // Load vào layout.hbs
    }
}

package com.example.Project.controller.development;

import java.util.List;

import com.example.Project.service.IToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Project.model.Category;
import com.example.Project.service.ICategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("development")
public class JsonPrettifyController {
    @Autowired
    private ICategoryService _categoryService;

    @Autowired
    private IToolService _toolService;

    @GetMapping("/json-prettify")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_USER"));

        if (isUser && _toolService.isPremiumToolByName("JSON Prettify and Format")) {
            return "404";
        }

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "JSON Prettify Tool");
        model.addAttribute("body", "json-prettify"); // Chỉ truyền tên template con
        return "layout"; // Trả về layout.hbs
    }
}
package com.example.Project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Project.model.Category;
import com.example.Project.model.Tool;
import com.example.Project.service.CategoryService;
import com.example.Project.service.FavoriteService;
import com.example.Project.service.ToolService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class HomeController {
    @Autowired
    private CategoryController categoryController;
    @Autowired
    private ToolController toolController;
    @Autowired
    private FavoriteController favoriteController;

    @GetMapping("")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        if (username != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            model.addAttribute("userId", userId);

            // Lấy danh sách công cụ yêu thích của user
            List<Tool> favoriteTools = favoriteController.getAllFavoritesByUserId(userId);
            model.addAttribute("favoriteTools", favoriteTools);
        }

        // Lấy danh sách categories từ Service
        List<Category> allCategories = categoryController.getAllCategories();
        model.addAttribute("categories", allCategories);

        // Lấy danh sách tất cả công cụ
        List<Tool> allTools = toolController.getAllTools();
        model.addAttribute("tools", allTools);

        // Trả về layout chính, chỉ truyền body là "home"
        model.addAttribute("title", "Home Page");
        model.addAttribute("body", "home");

        return "layout";
    }
}

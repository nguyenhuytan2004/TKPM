package com.example.Project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Project.model.Category;
import com.example.Project.model.Tool;
import com.example.Project.service.ICategoryService;
import com.example.Project.service.IFavoriteService;
import com.example.Project.service.IToolService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class HomeController {
    @Autowired
    private ICategoryService _categoryService;
    @Autowired
    private IToolService _toolService;
    @Autowired
    private IFavoriteService _favoriteService;

    @GetMapping("")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        if (username != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            model.addAttribute("userId", userId);

            // Lấy danh sách công cụ yêu thích của user
            List<Tool> favoriteTools = _favoriteService.getAllFavoriteToolsByUserId(userId);
            model.addAttribute("favoriteTools", favoriteTools);
        }

        // Lấy danh sách categories từ Service
        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);

        // Lấy danh sách tất cả công cụ
        List<Tool> allTools = _toolService.getAllTools();
        model.addAttribute("tools", allTools);

        // Trả về layout chính, chỉ truyền body là "home"
        model.addAttribute("title", "Home Page");
        model.addAttribute("body", "home");

        return "layout";
    }
}

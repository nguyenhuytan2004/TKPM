package com.example.Project.controller.math;


import com.example.Project.model.Category;
import com.example.Project.service.ICategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/math")
public class PercentageCalculatorController {

    @Autowired
    private ICategoryService _categoryService;

    @GetMapping("/percentage")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Percentage Calculator");
        model.addAttribute("body", "percentage");
        return "layout";
    }
}
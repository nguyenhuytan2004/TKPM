package com.example.Project.controller.crypto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;

@Controller
@RequestMapping("")
public class TokenGeneratorController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/crypto/token-generator")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "token-generator";
    }
}

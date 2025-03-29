package com.example.Project.controller.development;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;

@Controller
@RequestMapping("development")
public class JsonPrettifyController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/json-prettify")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "JSON Prettify Tool");
        model.addAttribute("body", "json-prettify");  // Chỉ truyền tên template con
        return "layout";  // Trả về layout.hbs
    }
}
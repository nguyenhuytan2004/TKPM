package com.example.Project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;

@Controller
@RequestMapping("")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
}

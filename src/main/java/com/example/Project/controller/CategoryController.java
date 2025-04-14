package com.example.Project.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Category;
import com.example.Project.service.ICategoryService;

@Controller
@RequestMapping("")
public class CategoryController {

    @Autowired
    private ICategoryService _categoryService;

    @RestController
    @RequestMapping("/api")
    public class CategoryHandler {

        @PostMapping("/category")
        public boolean addCategory(@RequestBody Map<String, Object> requestBody) {
            try {
                Category category = new Category();
                category.setName((String) requestBody.get("name"));
                category.setDescription((String) requestBody.get("description"));
                _categoryService.updateCategory(category);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @GetMapping("/categories")
        public List<Category> getCategories() {
            return _categoryService.getAllCategories();
        }
    }
}

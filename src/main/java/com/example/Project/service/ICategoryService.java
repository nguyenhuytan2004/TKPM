package com.example.Project.service;

import java.util.List;

import com.example.Project.model.Category;

public interface ICategoryService {

    public List<Category> getAllCategories();

    public Category getCategoryById(Integer id);

    public void updateCategory(Category category);
}

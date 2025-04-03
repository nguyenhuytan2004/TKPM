package com.example.Project.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Project.model.Category;
import com.example.Project.repository.ICategoryRepository;
import com.example.Project.service.ICategoryService;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository _categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return _categoryRepository.findAll();
    }
}

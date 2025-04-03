package com.example.Project.controller.measurement;

import com.example.Project.model.Category;
import com.example.Project.service.ICategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/measurement")
public class ChronometerController {

    @Autowired
    private ICategoryService _categoryService;

    @GetMapping("/chronometer")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Chronometer");
        model.addAttribute("body", "chronometer");
        return "layout";
    }
}
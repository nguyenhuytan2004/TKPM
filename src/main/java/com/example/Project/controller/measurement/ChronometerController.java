package com.example.Project.controller.measurement;

import com.example.Project.model.Category;
import com.example.Project.service.ICategoryService;
import com.example.Project.service.IToolService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("/measurement")
public class ChronometerController {

    @Autowired
    private ICategoryService _categoryService;

    @Autowired
    private IToolService _toolService;

    @GetMapping("/chronometer")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_USER"));

        if (isUser && _toolService.isPremiumToolByName("Chronometer")) {
            return "404";
        }

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Chronometer");
        model.addAttribute("body", "chronometer");
        return "layout";
    }
}
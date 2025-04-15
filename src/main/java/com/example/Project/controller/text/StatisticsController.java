package com.example.Project.controller.text;

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

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

@Controller
@RequestMapping("/text")
public class StatisticsController {

    @Autowired
    private ICategoryService _categoryService;

    @Autowired
    private IToolService _toolService;

    @GetMapping("/statistics")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_USER"));

        if (isUser && _toolService.isPremiumToolByName("Text Statistics")) {
            return "404";
        }

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Text Statistics");
        model.addAttribute("body", "statistics");

        return "layout";
    }

    @RestController
    @RequestMapping("/api/text")
    public static class StatisticsHandler {

        @PostMapping("/statistics")
        public Map<String, Object> getTextStatistics(@RequestBody Map<String, String> payload) {
            try {
                String text = payload.getOrDefault("content", "");
                int characterCount = text.length();
                int wordCount = text.isBlank() ? 0 : text.trim().split("\\s+").length;
                int lineCount = text.split("\r\n|\r|\n").length;
                int byteSize = text.getBytes(StandardCharsets.UTF_8).length;

                Map<String, Object> response = new HashMap<>();
                response.put("characterCount", characterCount);
                response.put("wordCount", wordCount);
                response.put("lineCount", lineCount);
                response.put("byteSize", byteSize);

                return response;
            } catch (Exception e) {
                return Collections.singletonMap("error", "Invalid input data");
            }
        }
    }
}

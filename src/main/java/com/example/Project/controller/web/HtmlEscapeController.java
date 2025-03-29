package com.example.Project.controller.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class HtmlEscapeController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/web/html-escape")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "HTML Escape Tool");
        model.addAttribute("body", "html-escape");
        return "layout";
    }

    @RestController
    @RequestMapping("/api/web/html-escape")
    public class HtmlEscapeHandler {
        @GetMapping("")
        public Map<String, String> escapeHtml(@RequestParam String string) {
            @SuppressWarnings("deprecation")
            String escapedString = StringEscapeUtils.escapeHtml4(string);
            return Collections.singletonMap("stringEscaped", escapedString);
        }

        @GetMapping("/unescape")
        public Map<String, String> unescapeHtml(@RequestParam String escapedString) {
            @SuppressWarnings("deprecation")
            String decodedString = StringEscapeUtils.unescapeHtml4(escapedString);
            return Collections.singletonMap("stringUnescaped", decodedString);
        }
    }
}

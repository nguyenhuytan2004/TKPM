package com.example.Project.controller.web;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.example.Project.service.IToolService;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Category;
import com.example.Project.service.ICategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("")
public class HtmlEscapeController {
    @Autowired
    private ICategoryService _categoryService;

    @Autowired
    private IToolService _toolService;

    @GetMapping("/web/html-escape")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_USER"));

        if (isUser && _toolService.isPremiumToolByName("Escape HTML Entities")) {
            return "404";
        }

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "HTML Escape Tool");
        model.addAttribute("body", "html-escape");
        return "layout";
    }

    @RestController
    @RequestMapping("/api/web/html-escape")
    public class HtmlEscapeApiController {
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

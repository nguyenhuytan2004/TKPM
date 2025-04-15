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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

@Controller
@RequestMapping("/text")
public class StringObfuscatorContronller {

    @Autowired
    private ICategoryService _categoryService;

    @Autowired
    private IToolService _toolService;

    @GetMapping("/string-obfuscator")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_USER"));

        if (isUser && _toolService.isPremiumToolByName("String obfuscator")) {
            return "404";
        }

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "String Obfuscator");
        model.addAttribute("body", "string-obfuscator");

        return "layout";
    }

    @RestController
    @RequestMapping("/api/text")
    public static class StringObfuscateHandler {

        @PostMapping("/string-obfuscator")
        public Map<String, Object> obfuscateString(@RequestBody Map<String, Object> payload) {
            try {
                String text = (String) payload.getOrDefault("text", "");
                int keepFirst = ((Number) payload.getOrDefault("keepFirst", 0)).intValue();
                int keepLast = ((Number) payload.getOrDefault("keepLast", 0)).intValue();
                boolean keepSpaces = (boolean) payload.getOrDefault("keepSpaces", true);

                String obfuscated = obfuscateText(text, keepFirst, keepLast, keepSpaces);

                Map<String, Object> response = new HashMap<>();
                response.put("result", obfuscated);
                return response;
            } catch (Exception e) {
                return Collections.singletonMap("error", "Invalid input data");
            }
        }

        private String obfuscateText(String text, int keepFirst, int keepLast, boolean keepSpaces) {
            if (text.isEmpty() || keepFirst + keepLast >= text.length()) {
                return text;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(text.substring(0, keepFirst)); // Giữ phần đầu

            for (int i = keepFirst; i < text.length() - keepLast; i++) {
                sb.append(keepSpaces && text.charAt(i) == ' ' ? ' ' : '*'); // Thay thế ký tự bằng '*', giữ khoảng trắng nếu cần
            }

            sb.append(text.substring(text.length() - keepLast)); // Giữ phần cuối
            return sb.toString();
        }
    }
}

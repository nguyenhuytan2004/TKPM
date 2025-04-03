package com.example.Project.controller.text;

import com.example.Project.model.Category;
import com.example.Project.service.ICategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/string-obfuscator")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

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

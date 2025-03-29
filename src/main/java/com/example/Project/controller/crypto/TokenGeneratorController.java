package com.example.Project.controller.crypto;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("crypto")
public class TokenGeneratorController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/token-generator")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Token Generator Tool"); // Thêm tiêu đề
        model.addAttribute("body", "token-generator"); // Load trang con
        return "layout"; // Load vào layout.hbs
    }

    @RestController
    @RequestMapping("api/crypto/token-generator")
    public class TokenGeneratorHandler {
        @PostMapping("")
        public Map<String, String> generateToken(@RequestBody Map<String, Object> payload) {
            boolean uppercase = (boolean) payload.getOrDefault("uppercase", false);
            boolean lowercase = (boolean) payload.getOrDefault("lowercase", false);
            boolean numbers = (boolean) payload.getOrDefault("numbers", false);
            boolean symbols = (boolean) payload.getOrDefault("symbols", false);

            int length;
            try {
                length = Integer.parseInt(payload.getOrDefault("length", "16").toString());
            } catch (NumberFormatException e) {
                length = 16; // Giá trị mặc định nếu parse lỗi
            }

            String chars = (uppercase ? "ABCDEFGHIJKLMNOPQRSTUVWXYZ" : "") +
                    (lowercase ? "abcdefghijklmnopqrstuvwxyz" : "") +
                    (numbers ? "0123456789" : "") +
                    (symbols ? "!@#$%^&*(),<.>/?;:'\"\\|-_=+" : "");

            if (chars.isEmpty()) {
                return Collections.singletonMap("token", "ERROR: No character set selected");
            }

            StringBuilder token = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < length; i++) {
                token.append(chars.charAt(random.nextInt(chars.length())));
            }

            return Collections.singletonMap("token", token.toString());
        }
    }

}
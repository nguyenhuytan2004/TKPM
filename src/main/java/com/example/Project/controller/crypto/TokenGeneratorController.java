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
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;

@Controller
@RequestMapping("")
public class TokenGeneratorController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/crypto/token-generator")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "token-generator";
    }

    @PostMapping("/api/crypto/token-generator")
    @ResponseBody
    public Map<String, String> generateToken(@RequestBody Map<String, Object> payload) {
        boolean uppercase = (boolean) payload.get("uppercase");
        boolean lowercase = (boolean) payload.get("lowercase");
        boolean numbers = (boolean) payload.get("numbers");
        boolean symbols = (boolean) payload.get("symbols");
        int length = Integer.parseInt((String) payload.get("length"));

        String chars = (uppercase ? "ABCDEFGHIJKLMNOPQRSTUVWXYZ" : "") +
                (lowercase ? "abcdefghijklmnopqrstuvwxyz" : "") +
                (numbers ? "0123456789" : "") +
                (symbols ? "!@#$%^&*(),<.>/?;:'\"\\|-_=+" : "");

        if (chars.isEmpty()) {
            return Collections.singletonMap("token", "");
        }

        StringBuilder token = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            token.append(chars.charAt(random.nextInt(chars.length())));
        }

        return Collections.singletonMap("token", token.toString());
    }
}

package com.example.Project.controller.crypto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("crypto")
public class BcryptController {
    @Autowired
    private ICategoryService _categoryService;

    @GetMapping("/bcrypt")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Bcrypt Hashing Tool");
        model.addAttribute("body", "bcrypt"); // Load template con
        return "layout"; // Trả về layout.hbs
    }

    // API xử lý bcrypt
    @RestController
    @RequestMapping("/api/crypto/bcrypt")
    public static class BcryptApiController {

        @GetMapping("/hash")
        public Map<String, String> hashUsingBcrypt(
                @RequestParam String stringToHash,
                @RequestParam String saltCount) {

            try {
                int salt = Integer.parseInt(saltCount);
                String hashed = BCrypt.hashpw(stringToHash, BCrypt.gensalt(salt));
                return Collections.singletonMap("hashedString", hashed);
            } catch (NumberFormatException e) {
                return Collections.singletonMap("error", "Invalid input");
            }
        }

        @GetMapping("/compare")
        public Map<String, Boolean> compareHash(@RequestParam String string, @RequestParam String hash) {
            if (hash.isEmpty()) {
                return Collections.singletonMap("match", false);
            }
            boolean match = BCrypt.checkpw(string, hash);
            return Collections.singletonMap("match", match);
        }
    }
}

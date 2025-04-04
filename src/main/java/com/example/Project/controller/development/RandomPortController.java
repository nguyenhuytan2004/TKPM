package com.example.Project.controller.development;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Category;
import com.example.Project.service.ICategoryService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("development")
public class RandomPortController {
    @Autowired
    private ICategoryService _categoryService;

    @GetMapping("/random-port")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Random Port Generator"); // Thêm tiêu đề
        model.addAttribute("body", "random-port"); // Load trang con
        return "layout"; // Load vào layout.hbs
    }

    @RestController
    @RequestMapping("api/development/random-port")
    public class RandomPortApiController {
        @GetMapping("")
        public Map<String, Integer> randomPort() {
            int port = ThreadLocalRandom.current().nextInt(1024, 65536);
            return Collections.singletonMap("portNumber", port);
        }
    }
}

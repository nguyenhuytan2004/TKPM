package com.example.Project.controller.development;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;

@Controller
@RequestMapping("development")
public class RandomPortController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/random-port")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Random Port Generator");  // Thêm tiêu đề
        model.addAttribute("body", "random-port");  // Load trang con
        return "layout";  // Load vào layout.hbs
    }
}

@RestController
@RequestMapping("api/development/random-port")
class RandomPortApiController {
    @GetMapping("/generate")
    public Map<String, Integer> randomPort() {
        int port = ThreadLocalRandom.current().nextInt(1024, 65536);
        return Collections.singletonMap("portNumber", port);
    }
}

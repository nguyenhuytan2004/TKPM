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
import com.example.Project.service.CategoryService;

@Controller
@RequestMapping("")
public class RandomPortController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/development/random-port")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "random-port";
    }

    @RestController
    @RequestMapping("/api/development/random-port")
    public class RandomPortHandler {
        @GetMapping("")
        public Map<String, Integer> randomPort() {
            return Collections.singletonMap("portNumber", ThreadLocalRandom.current().nextInt(1024, 65536));
        }
    }
}

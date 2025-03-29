package com.example.Project.controller.web;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class UrlEncodeDecodeController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/web/url-encode-decode")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "URL Encode/Decode Tool");
        model.addAttribute("body", "url-encode-decode");
        return "layout";
    }

    @RestController
    @RequestMapping("/api/web/url-encode-decode")
    public class UrlEncodeDecodeHandler {
        @GetMapping("/encode")
        public Map<String, String> encodeTextToUrl(@RequestParam String string) {
            String encodedString = URLEncoder.encode(string, StandardCharsets.UTF_8);
            return Collections.singletonMap("encodedString", encodedString);
        }

        @GetMapping("/decode")
        public Map<String, Object> decodeUrlToText(@RequestParam String encodedString) {
            Map<String, Object> result = new HashMap<>();

            String decodedString = URLDecoder.decode(encodedString, StandardCharsets.UTF_8);
            result.put("isSuccess", true);
            result.put("decodedString", decodedString);

            return result;
        }
    }
}

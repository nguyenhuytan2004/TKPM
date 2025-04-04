package com.example.Project.controller.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
@RequestMapping("")
public class UrlParserController {
    @Autowired
    private ICategoryService _categoryService;

    @GetMapping("/web/url-parser")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "URL Parser");
        model.addAttribute("body", "url-parser");
        return "layout";
    }

    @RestController
    @RequestMapping("/api/web/url-parser")
    public class UrlParserApiController {
        @GetMapping("")
        public Map<String, Object> parseUrl(String urlString) {
            Map<String, Object> result = new HashMap<>();
            try {
                URI uri = new URI(urlString);

                String userInfo = uri.getUserInfo();
                String username = "";
                String password = "";
                if (userInfo != null && userInfo.contains(":")) {
                    String[] parts = userInfo.split(":", 2);
                    username = parts[0];
                    password = parts[1];
                } else if (userInfo != null) {
                    username = userInfo;
                }

                result.put("protocol", uri.getScheme() != null ? uri.getScheme() : "");
                result.put("username", username);
                result.put("password", password);
                result.put("hostname", uri.getHost() != null ? uri.getHost() : "");
                result.put("port", uri.getPort() != -1 ? String.valueOf(uri.getPort()) : "");
                result.put("path", uri.getPath() != null ? uri.getPath() : "");
                result.put("params", uri.getQuery() != null ? uri.getQuery() : "");
                result.put("fragment", uri.getFragment() != null ? uri.getFragment() : "");

                result.put("isSuccess", true);

            } catch (URISyntaxException e) {
                result.put("isSuccess", false);

            }
            return result;
        }
    }
}

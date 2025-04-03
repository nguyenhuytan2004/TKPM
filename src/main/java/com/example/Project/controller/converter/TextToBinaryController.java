package com.example.Project.controller.converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@RequestMapping("converter")
public class TextToBinaryController {
    @Autowired
    private ICategoryService _categoryService;

    @GetMapping("/text-to-binary")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Text to Binary Converter"); // Thêm title
        model.addAttribute("body", "text-to-binary"); // Load template con
        return "layout"; // Load vào layout.hbs
    }

    @RestController
    @RequestMapping("/api/converter/text-to-binary")
    public class TextToBinaryHandler {
        @GetMapping("")
        public Map<String, String> convertTextToBinary(@RequestParam String textString) {
            String binaryString = textString.chars()
                    .mapToObj(c -> String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'))
                    .collect(Collectors.joining(" "));
            return Collections.singletonMap("binaryFromText", binaryString);
        }

        @GetMapping("/reverse")
        public Map<String, Object> convertBinaryToText(@RequestParam String binaryString) {
            Map<String, Object> response = new HashMap<>();

            if (!binaryString.matches("[01 ]+") || binaryString.replace(" ", "").length() % 8 != 0) {
                response.put("isSuccess", false);
                response.put("textFromBinary", "");
                return response;
            }

            String textString = Arrays.stream(binaryString.split(" "))
                    .map(bin -> (char) Integer.parseInt(bin, 2))
                    .map(String::valueOf)
                    .collect(Collectors.joining());
            response.put("isSuccess", true);
            response.put("textFromBinary", textString);

            return response;
        }
    }
}

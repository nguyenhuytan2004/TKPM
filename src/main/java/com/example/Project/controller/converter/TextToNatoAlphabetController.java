package com.example.Project.controller.converter;

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
@RequestMapping("converter")
public class TextToNatoAlphabetController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/text-to-nato-alphabet")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Text to NATO Alphabet Converter"); // Thêm title
        model.addAttribute("body", "text-to-nato-alphabet"); // Load template con
        return "layout"; // Load vào layout.hbs
    }

    @RestController
    @RequestMapping("/api/converter/text-to-nato-alphabet")
    public class TextToNatoAlphabetHandler {
        private static final Map<Character, String> NATO_ALPHABET = Map.ofEntries(
                Map.entry('A', "Alpha"), Map.entry('B', "Bravo"), Map.entry('C', "Charlie"),
                Map.entry('D', "Delta"), Map.entry('E', "Echo"), Map.entry('F', "Foxtrot"),
                Map.entry('G', "Golf"), Map.entry('H', "Hotel"), Map.entry('I', "India"),
                Map.entry('J', "Juliett"), Map.entry('K', "Kilo"), Map.entry('L', "Lima"),
                Map.entry('M', "Mike"), Map.entry('N', "November"), Map.entry('O', "Oscar"),
                Map.entry('P', "Papa"), Map.entry('Q', "Quebec"), Map.entry('R', "Romeo"),
                Map.entry('S', "Sierra"), Map.entry('T', "Tango"), Map.entry('U', "Uniform"),
                Map.entry('V', "Victor"), Map.entry('W', "Whiskey"), Map.entry('X', "X-ray"),
                Map.entry('Y', "Yankee"), Map.entry('Z', "Zulu"));

        @GetMapping("")
        public Map<String, String> convertTextToNatoAlphabet(@RequestParam String string) {
            StringBuilder natoString = new StringBuilder();
            for (char ch : string.toUpperCase().toCharArray()) {
                natoString.append(NATO_ALPHABET.getOrDefault(ch, String.valueOf(ch))).append(" ");
            }
            return Map.of("convertedString", natoString.toString().trim());
        }
    }
}

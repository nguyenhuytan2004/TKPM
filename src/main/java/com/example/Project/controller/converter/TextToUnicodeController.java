package com.example.Project.controller.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.parser.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;

@Controller
@RequestMapping("")
public class TextToUnicodeController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/converter/text-to-unicode")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "text-to-unicode";
    }

    @RestController
    @RequestMapping("/api/converter/text-to-unicode")
    public class TextToUnicodeHandler {
        @GetMapping("")
        public Map<String, String> convertTextToUnicode(@RequestParam String textString) {
            String unicodeString = textString.chars()
                    .mapToObj(c -> "&#" + c + ";")
                    .collect(Collectors.joining(""));
            return Collections.singletonMap("unicodeFromText", unicodeString);
        }

        @GetMapping("/reverse")
        public Map<String, Object> convertUnicodeToText(@RequestParam String unicodeString) {
            Map<String, Object> response = new HashMap<>();

            if (!unicodeString.matches("(&#\\d+;\\s*)+")) {
                response.put("isSuccess", false);
                response.put("textFromBinary", "");
                return response;
            }

            Pattern pattern = Pattern.compile("(&#\\d+;|\\s+)");
            Matcher matcher = pattern.matcher(unicodeString);

            StringBuilder textString = new StringBuilder();
            while (matcher.find()) {
                String token = matcher.group();
                if (token.startsWith("&#")) {
                    textString.append(Parser.unescapeEntities(token, false));
                } else {
                    textString.append(token);
                }
            }

            response.put("isSuccess", true);
            response.put("textFromUnicode", textString);

            return response;
        }
    }
}

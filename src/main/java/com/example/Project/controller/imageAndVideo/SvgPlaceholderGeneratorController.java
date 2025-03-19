package com.example.Project.controller.imageAndVideo;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class SvgPlaceholderGeneratorController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/images-videos/svg-placeholder-generator")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        return "svg-placeholder-generator";
    }

    @RestController
    @RequestMapping("/api/images-videos/svg-placeholder-generator")
    public class SvgPlaceholderGeneratorHandler {
        @GetMapping("")
        public ResponseEntity<Map<String, Object>> generateSvgPlaceholder(@RequestParam(defaultValue = "0") int width,
                @RequestParam(defaultValue = "0") int height,
                @RequestParam(defaultValue = "0") int fontSize, @RequestParam String bgColor,
                @RequestParam String textColor,
                @RequestParam String text) {

            Map<String, Object> response = new HashMap<>();

            try {
                String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='" + width + "' height='" + height
                        + "'>\n\t"
                        + "<rect width='" + width + "' height='" + height + "' fill='" + bgColor + "'/>\n\t"
                        + "<text x='50%' y='50%' dominant-baseline='middle' text-anchor='middle' "
                        + "font-family='monospace' font-size='" + fontSize + "px' fill='" + textColor + "'>"
                        + text + "</text>\n"
                        + "</svg>";

                String svgBase64 = Base64.getEncoder().encodeToString(svg.getBytes());

                response.put("svgInHtml", svg);
                response.put("svgInBase64", "data:image/svg+xml;base64," + svgBase64);
                response.put("svgImage", "data:image/svg+xml;base64," + svgBase64);
                response.put("isSuccess", true);
            } catch (Exception e) {
                response.put("isSuccess", false);
            }

            return ResponseEntity.ok(response);
        }
    }
}

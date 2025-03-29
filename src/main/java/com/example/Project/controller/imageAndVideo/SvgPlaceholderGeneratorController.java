package com.example.Project.controller.imageAndVideo;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.Project.model.Category;
import com.example.Project.service.CategoryService;

@Controller
@RequestMapping("images-videos")
public class SvgPlaceholderGeneratorController {
    @Autowired
    private CategoryService categoriesService;

    @GetMapping("/svg-placeholder-generator")
    public String show(Model model) {
        List<Category> allCategories = categoriesService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "SVG Placeholder Generator");
        model.addAttribute("body", "svg-placeholder-generator");
        return "layout";
    }
}

@RestController
@RequestMapping("/api/images-videos/svg-placeholder-generator")
class SvgPlaceholderGeneratorApiController {
    @GetMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateSvgPlaceholder(
            @RequestParam(defaultValue = "100") int width,
            @RequestParam(defaultValue = "100") int height,
            @RequestParam(defaultValue = "16") int fontSize,
            @RequestParam(defaultValue = "#CCCCCC") String bgColor,
            @RequestParam(defaultValue = "#000000") String textColor,
            @RequestParam(defaultValue = "Placeholder") String text) {

        Map<String, Object> response = new HashMap<>();

        try {
            String svg = "<svg xmlns='http://www.w3.org/2000/svg' width='" + width + "' height='" + height + "'>\n\t"
                    + "<rect width='" + width + "' height='" + height + "' fill='" + bgColor + "'/>\n\t"
                    + "<text x='50%' y='50%' dominant-baseline='middle' text-anchor='middle' "
                    + "font-family='monospace' font-size='" + fontSize + "px' fill='" + textColor + "'>"
                    + text + "</text>\n"
                    + "</svg>";

            String svgBase64 = Base64.getEncoder().encodeToString(svg.getBytes());

            response.put("svgInHtml", svg);
            response.put("svgInBase64", "data:image/svg+xml;base64," + svgBase64);
            response.put("isSuccess", true);
        } catch (Exception e) {
            response.put("isSuccess", false);
        }

        return ResponseEntity.ok(response);
    }
}

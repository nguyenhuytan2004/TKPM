package com.example.Project.controller.math;

import com.example.Project.model.Category;
import com.example.Project.service.ICategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.Collections;

@Controller
@RequestMapping("/math")
public class ETACalculatorController {

    @Autowired
    private ICategoryService _categoryService;

    @GetMapping("/eta-calculator")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "ETA Calculator");
        model.addAttribute("body", "eta-calculator");
        return "layout";
    }

    @RestController
    @RequestMapping("/api/math")
    public static class ETACalculatorHandler {

        @PostMapping("/calculate-eta")
        public Map<String, Object> calculateETA(@RequestBody Map<String, Object> payload) {
            try {
                double totalElements = Double.parseDouble(payload.get("totalElements").toString());
                double rate = Double.parseDouble(payload.get("rate").toString());
                double timeSpan = Double.parseDouble(payload.get("timeSpan").toString());
                String timeUnit = payload.get("timeUnit").toString();
                String startTimeStr = payload.get("startTime").toString();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime startTime = LocalDateTime.parse(startTimeStr, formatter);

                double totalTime = (totalElements / rate) * timeSpan;
                long totalMillis = convertToMillis(totalTime, timeUnit);

                LocalDateTime endTime = startTime.plusNanos(TimeUnit.MILLISECONDS.toNanos(totalMillis));

                String formattedEndTime = endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                return Map.of(
                        "duration", totalTime,
                        "unit", timeUnit,
                        "endTime", formattedEndTime
                );
            } catch (Exception e) {
                return Collections.singletonMap("error", "Invalid input data");
            }
        }

        private long convertToMillis(double time, String unit) {
            return switch (unit) {
                case "seconds" -> (long) (time * 1000);
                case "minutes" -> (long) (time * 60 * 1000);
                case "hours" -> (long) (time * 60 * 60 * 1000);
                case "days" -> (long) (time * 24 * 60 * 60 * 1000);
                default -> throw new IllegalArgumentException("Invalid time unit");
            };
        }
    }
}

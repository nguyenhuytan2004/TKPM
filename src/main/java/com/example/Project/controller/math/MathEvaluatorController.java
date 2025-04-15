package com.example.Project.controller.math;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.example.Project.model.Category;
import com.example.Project.service.ICategoryService;
import com.example.Project.service.IToolService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

@Controller
@RequestMapping("/math")
public class MathEvaluatorController {

    @Autowired
    private ICategoryService _categoryService;

    @Autowired
    private IToolService _toolService;

    @GetMapping("/evaluator")
    public String show(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_USER"));

        if (isUser && _toolService.isPremiumToolByName("Math Evaluator")) {
            return "404";
        }

        List<Category> allCategories = _categoryService.getAllCategories();
        model.addAttribute("categories", allCategories);
        model.addAttribute("title", "Math Evaluator");
        model.addAttribute("body", "evaluator"); // Load template con
        return "layout"; // Trả về layout.hbs
    }

    @RestController
    @RequestMapping("/api/math")
    public static class MathEvaluatorHandler {

        @GetMapping("/evaluate")
        public Map<String, Object> evaluateExpression(@RequestParam String expr) {
            try {
                // Tự động thêm "Math." trước các hàm toán học
                expr = expr.replaceAll("\\b(sqrt|cos|sin|abs)\\b", "Math.$1");


                ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
                Object result = engine.eval(expr);
                return Collections.singletonMap("result", result);
            } catch (ScriptException | NullPointerException e) {
                return Collections.singletonMap("error", "Invalid expression");
            }
        }
    }
}

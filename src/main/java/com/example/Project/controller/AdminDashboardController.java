package com.example.Project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

import com.example.Project.model.Category;
import com.example.Project.model.Role;
import com.example.Project.model.Tool;
import com.example.Project.model.User;
import com.example.Project.service.ICategoryService;
import com.example.Project.service.IFavoriteService;
import com.example.Project.service.IToolService;
import com.example.Project.service.IUserService;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private IToolService _toolService;
    @Autowired
    private ICategoryService _categoryService;
    @Autowired
    private IUserService _userService;
    @Autowired
    private IFavoriteService _favoriteService;  // Dịch vụ yêu thích người dùng

    @GetMapping("/tool/management")
    public String showToolManagement(Model model, HttpSession session) {
        // Lấy thông tin người dùng từ session
        String username = (String) session.getAttribute("username");
        Integer userId = (Integer) session.getAttribute("userId");

        // Thêm thông tin người dùng vào model
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);

        // Lấy danh sách công cụ
        List<Tool> tools = _toolService.getAllTools();
        List<Map<String, Object>> toolList = new ArrayList<>();
        for (Tool tool : tools) {
            Map<String, Object> toolData = new HashMap<>();
            toolData.put("id", tool.getId());
            toolData.put("name", tool.getName());
            toolData.put("description", tool.getDescription());
            toolData.put("categoryName", tool.getCategory().getName());
            toolData.put("is_premium", tool.getIsPremium());
            toolData.put("is_active", tool.isActive());
            toolList.add(toolData);
        }

        // Lấy danh sách các category
        List<Category> categories = _categoryService.getAllCategories();

        model.addAttribute("tools", toolList);
        model.addAttribute("categories", categories);

        model.addAttribute("title", "Tool Management");
        model.addAttribute("body", "tool_management");

        return "layout_admin";
    }

    @GetMapping("/user/management")
    public String showUserManagement(Model model, HttpSession session) {
        // Lấy thông tin người dùng từ session
        String username = (String) session.getAttribute("username");
        Integer userId = (Integer) session.getAttribute("userId");

        // Thêm thông tin người dùng vào model
        model.addAttribute("username", username);
        model.addAttribute("userId", userId);

        // Lấy danh sách người dùng
        List<User> users = _userService.getAllUsers();
        List<Map<String, Object>> userList = new ArrayList<>();
        for (User user : users) {
            if (user.getRole() == Role.ADMIN) {
                continue; // Không lấy admin
            }
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", user.getId());
            userData.put("username", user.getUsername());
            userData.put("isPremium", user.getRole() == Role.PREMIUM);
            userData.put("requirePremium", user.isRequirePremium());

            userList.add(userData);
        }

        model.addAttribute("users", userList);

        model.addAttribute("title", "User Management");
        model.addAttribute("body", "user_management");

        return "layout_admin";
    }
}

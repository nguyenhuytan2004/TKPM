package com.example.Project.controller;

import com.example.Project.model.User;
import com.example.Project.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class AuthController {
    @Autowired
    private IUserService _userService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showSignUpPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password,
                           @RequestParam String confirmPassword, Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        boolean isUserCreated = _userService.createUser(username, password);

        if (isUserCreated) {
            return "redirect:/user/login";
        } else {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
    }
}
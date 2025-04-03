package com.example.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Project.model.User;
import com.example.Project.service.IUserService;

import jakarta.servlet.http.HttpSession;

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

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
            Model model) {
        User user = _userService.authenticateUser(username, password);

        if (user != null) {
            session.setMaxInactiveInterval(1800);
            session.setAttribute("username", username);
            session.setAttribute("userId", user.getId());
            return "redirect:/"; // home.hbs
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

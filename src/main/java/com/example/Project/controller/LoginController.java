package com.example.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Project.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/login")
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String show() {
        return "login";
    }

    @PostMapping("")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session,
            Model model) {
        boolean isValidUser = userService.authenticate(username, password);

        if (isValidUser) {
            session.setAttribute("username", username);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Username or password is not valid");
            return "login";
        }
    }
}

package com.example.Project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/register")
public class RegisterController {
    @GetMapping("")
    public String show() {
        return "register";
    }
}

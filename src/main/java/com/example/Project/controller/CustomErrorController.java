package com.example.Project.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController{

    @GetMapping("/404")
    public String notFound() {
        return "404";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        return "404";
    }
}

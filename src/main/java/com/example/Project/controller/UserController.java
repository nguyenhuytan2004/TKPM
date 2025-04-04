package com.example.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService _userService;

    @RestController
    @RequestMapping("/api")
    public class UserApiController {

        @PutMapping("/user/{id}/approve-premium")
        public boolean approvePremium(@PathVariable int id) {
            return _userService.approvePremium(id);
        }

        @PutMapping("/user/{id}/reject-premium")
        public boolean rejectPremium(@PathVariable int id) {
            return _userService.rejectPremium(id);
        }

        @PutMapping("/user/{id}/downgrade")
        public boolean downgrade(@PathVariable int id) {
            return _userService.downgrade(id);
        }
    }
}

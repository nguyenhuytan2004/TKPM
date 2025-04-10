package com.example.Project.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

        @PatchMapping("/user/{id}/premium-request")
        public boolean updatePremiumRequest(
                @PathVariable int id,
                @RequestBody Map<String, String> requestBody) {

            String action = requestBody.get("action");

            if ("approve".equalsIgnoreCase(action)) {
                return _userService.approvePremium(id);
            } else if ("reject".equalsIgnoreCase(action)) {
                return _userService.rejectPremium(id);
            }

            return false;
        }

        @PutMapping("/user/{id}/premium-status")
        public boolean downgrade(@PathVariable int id, @RequestBody Map<String, String> requestBody) {
            String action = requestBody.get("action");
            if ("downgrade".equalsIgnoreCase(action)) {
                return _userService.downgrade(id);
            }

            return false;
        }
    }
}

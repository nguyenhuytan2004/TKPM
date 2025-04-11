package com.example.Project.controller;

import com.example.Project.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService _userService;

    @PatchMapping("/{id}/premium-request")
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

    @PutMapping("/{id}/premium-status")
    public boolean downgrade(@PathVariable int id, @RequestBody Map<String, String> requestBody) {
        String action = requestBody.get("action");
        if ("downgrade".equalsIgnoreCase(action)) {
            return _userService.downgrade(id);
        }

        return false;
    }
}

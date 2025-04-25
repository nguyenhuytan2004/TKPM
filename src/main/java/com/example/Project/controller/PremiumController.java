package com.example.Project.controller;


import com.example.Project.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/premium")
public class PremiumController {

    @Autowired
    private IUserService _UserService;


    @PostMapping("/{userId}")
    public ResponseEntity<?> requestUpgrade(@PathVariable Integer userId) {
        boolean success = _UserService.requestPremium(userId);
        if (success) {
            return ResponseEntity.ok("Premium upgrade requested");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}

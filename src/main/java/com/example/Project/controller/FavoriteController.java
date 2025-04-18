package com.example.Project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.service.IFavoriteService;

@Controller
@RequestMapping("/user/favorite-tool")
public class FavoriteController {
    @Autowired
    IFavoriteService _favoriteService;

    @RestController
    @RequestMapping("api/user/favorite-tool")
    public class FavoriteHandler {

        @PostMapping("")
        public boolean addFavorite(@RequestParam int userId, @RequestParam int toolId) {
            return _favoriteService.addFavorite(userId, toolId);
        }

        @DeleteMapping("")
        public boolean removeFavorite(@RequestParam int userId, @RequestParam int toolId) {
            return _favoriteService.removeFavorite(userId, toolId);
        }
    }
}

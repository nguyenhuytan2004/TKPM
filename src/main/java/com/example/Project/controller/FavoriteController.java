package com.example.Project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Project.model.Favorite;
import com.example.Project.model.Tool;
import com.example.Project.service.FavoriteService;

@Controller
@RequestMapping("/user/favorite-tool")
public class FavoriteController {
    @Autowired
    FavoriteService favoriteService;

    @Autowired
    ToolController toolController;

    public List<Tool> getAllFavoritesByUserId(int userId) {
        List<Tool> favoriteTools = new ArrayList<>();

        List<Favorite> favorites = favoriteService.getAllFavoritesByUserId(userId);
        for (Favorite favorite : favorites) {
            int toolId = favorite.getToolId();
            Tool tool = toolController.getToolById(toolId);
            favoriteTools.add(tool);
        }

        return favoriteTools;
    }

    @RestController
    @RequestMapping("api/user/favorite-tool")
    public class FavoriteHandler {
        @GetMapping("/add")
        public boolean addFavorite(@RequestParam int userId, @RequestParam int toolId) {
            return favoriteService.addFavorite(userId, toolId);
        }

        @GetMapping("/remove")
        public boolean removeFavorite(@RequestParam int userId, @RequestParam int toolId) {
            return favoriteService.removeFavorite(userId, toolId);
        }
    }
}

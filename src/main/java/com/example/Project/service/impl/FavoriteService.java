package com.example.Project.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Project.model.Favorite;
import com.example.Project.model.Tool;
import com.example.Project.repository.IFavoriteRepository;
import com.example.Project.service.IFavoriteService;
import com.example.Project.service.IToolService;

@Service
public class FavoriteService implements IFavoriteService {
    @Autowired
    private IFavoriteRepository _favoriteRepository;

    @Autowired
    private IToolService _toolService;

    @Override
    public List<Tool> getAllFavoriteToolsByUserId(int userId) {
        List<Tool> favoriteTools = new ArrayList<>();

        List<Favorite> favorites = _favoriteRepository.findAllByUserId(userId);
        for (Favorite favorite : favorites) {
            int toolId = favorite.getToolId();
            Tool tool = _toolService.getToolById(toolId);
            favoriteTools.add(tool);
        }

        return favoriteTools.stream()
                .filter(Tool::isActive)
                .toList();
    }

    @Override
    public boolean addFavorite(int userId, int toolId) {
        Optional<Favorite> favorite = _favoriteRepository.findByUserIdAndToolId(userId, toolId);
        if (favorite.isPresent()) {
            return false;
        }
        Favorite newFavorite = new Favorite();
        newFavorite.setUserId(userId);
        newFavorite.setToolId(toolId);
        _favoriteRepository.save(newFavorite);
        return true;
    }

    @Override
    public boolean removeFavorite(int userId, int toolId) {
        Optional<Favorite> favorite = _favoriteRepository.findByUserIdAndToolId(userId, toolId);
        if (!favorite.isPresent()) {
            return false;
        }
        _favoriteRepository.delete(favorite.get());
        return true;
    }
}

package com.example.Project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Project.model.Favorite;
import com.example.Project.repository.FavoriteRepository;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    public List<Favorite> getAllFavoritesByUserId(int userId) {
        return favoriteRepository.findAllByUserId(userId);
    }

    public boolean addFavorite(int userId, int toolId) {
        Optional<Favorite> favorite = favoriteRepository.findByUserIdAndToolId(userId, toolId);
        if (favorite.isPresent()) {
            return false;
        }
        Favorite newFavorite = new Favorite();
        newFavorite.setUserId(userId);
        newFavorite.setToolId(toolId);
        favoriteRepository.save(newFavorite);
        return true;
    }

    public boolean removeFavorite(int userId, int toolId) {
        Optional<Favorite> favorite = favoriteRepository.findByUserIdAndToolId(userId, toolId);
        if (!favorite.isPresent()) {
            return false;
        }
        favoriteRepository.delete(favorite.get());
        return true;
    }
}

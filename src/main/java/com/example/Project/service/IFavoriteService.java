package com.example.Project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Project.model.Tool;

@Service
public interface IFavoriteService {

    public List<Tool> getAllFavoriteToolsByUserId(int userId);

    public boolean addFavorite(int userId, int toolId);

    public boolean removeFavorite(int userId, int toolId);
}

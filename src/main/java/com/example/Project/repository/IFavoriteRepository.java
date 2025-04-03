package com.example.Project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Project.model.Favorite;

public interface IFavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserIdAndToolId(Integer userId, Integer toolId);

    List<Favorite> findAllByUserId(Integer userId);
}

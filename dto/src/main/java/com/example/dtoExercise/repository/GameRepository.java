package com.example.dtoExercise.repository;

import com.example.dtoExercise.model.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    Game findByTitle(String title);
}

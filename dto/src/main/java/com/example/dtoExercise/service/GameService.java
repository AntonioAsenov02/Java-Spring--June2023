package com.example.dtoExercise.service;

import com.example.dtoExercise.model.dto.GameAddDto;
import com.example.dtoExercise.model.dto.GameDetailsDto;
import com.example.dtoExercise.model.dto.GameRetrieveAllDto;

import java.math.BigDecimal;
import java.util.List;

public interface GameService {
    void addGame(GameAddDto gameAddDto);

    void editGame(long gameId, BigDecimal price, double size);

    void deleteGame(long gameId);

    List<GameRetrieveAllDto> getAllGames();

    GameDetailsDto getGameDetails(String title);
}

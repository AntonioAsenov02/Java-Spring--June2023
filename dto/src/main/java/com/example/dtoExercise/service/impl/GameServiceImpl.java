package com.example.dtoExercise.service.impl;

import com.example.dtoExercise.model.dto.GameAddDto;
import com.example.dtoExercise.model.dto.GameDetailsDto;
import com.example.dtoExercise.model.dto.GameRetrieveAllDto;
import com.example.dtoExercise.model.entity.Game;
import com.example.dtoExercise.repository.GameRepository;
import com.example.dtoExercise.service.GameService;
import com.example.dtoExercise.util.ValidationUtil;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public GameServiceImpl(GameRepository gameRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.gameRepository = gameRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }


    @Override
    public void addGame(GameAddDto gameAddDto) {

        Set<ConstraintViolation<GameAddDto>> violations = validationUtil.getViolations(gameAddDto);

        if (!violations.isEmpty()) {

            violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            return;
        }

        Game game = modelMapper.map(gameAddDto, Game.class);
        game.setReleaseDate(LocalDate.parse(gameAddDto.getReleaseDate(),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")));

        gameRepository.save(game);

        System.out.println("Added game " + gameAddDto.getTitle());
    }

    @Override
    public void editGame(long gameId, BigDecimal price, double size) {

        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            System.out.println("Enter valid id!");
            return;
        }

        game.setPrice(price);
        game.setSize(size);

        gameRepository.save(game);

        System.out.println("Edited " + game.getTitle());
    }

    @Override
    public void deleteGame(long gameId) {

        Game game = gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            System.out.println("Enter valid id!");
            return;
        }

        gameRepository.delete(game);

        System.out.println("Deleted " + game.getTitle());
    }

    @Override
    public List<GameRetrieveAllDto> getAllGames() {

        return gameRepository.findAll()
                .stream()
                .map(g -> modelMapper.map(g, GameRetrieveAllDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public GameDetailsDto getGameDetails(String title) {

        Game game = gameRepository.findByTitle(title);

        GameDetailsDto gameDetailsDto = null;

        if (game != null) {
            gameDetailsDto = modelMapper.map(game, GameDetailsDto.class);
        }

        return gameDetailsDto;
    }
}

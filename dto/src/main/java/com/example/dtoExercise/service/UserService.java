package com.example.dtoExercise.service;

import com.example.dtoExercise.model.dto.GameOwnedDto;
import com.example.dtoExercise.model.dto.UserLoginDto;
import com.example.dtoExercise.model.dto.UserRegisterDto;

import java.util.List;

public interface UserService {
    void registerUser(UserRegisterDto userRegisterDto);

    void loginUser(UserLoginDto userLoginDto);

    void logoutUser();

    List<GameOwnedDto> findOwnedGames();
}

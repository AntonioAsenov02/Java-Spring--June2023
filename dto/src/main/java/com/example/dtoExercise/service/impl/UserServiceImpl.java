package com.example.dtoExercise.service.impl;

import com.example.dtoExercise.model.dto.GameOwnedDto;
import com.example.dtoExercise.model.dto.UserLoginDto;
import com.example.dtoExercise.model.dto.UserRegisterDto;
import com.example.dtoExercise.model.entity.User;
import com.example.dtoExercise.repository.UserRepository;
import com.example.dtoExercise.service.UserService;
import com.example.dtoExercise.util.ValidationUtil;
import jakarta.validation.ConstraintViolation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private User loggedInUser;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {

        if (!userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword())) {
            System.out.println("Wrong confirmation password!");
            return;
        }

        Set<ConstraintViolation<UserRegisterDto>> violations =
                validationUtil.getViolations(userRegisterDto);

        if (!violations.isEmpty()) {

            violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            return;
        }

        User user = modelMapper.map(userRegisterDto, User.class);

        userRepository.save(user);
    }

    @Override
    public void loginUser(UserLoginDto userLoginDto) {

        Set<ConstraintViolation<UserLoginDto>> violations =
                validationUtil.getViolations(userLoginDto);

        if (!violations.isEmpty()) {
            violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .forEach(System.out::println);

            return;
        }

        User user = userRepository.findByEmailAndPassword(
                        userLoginDto.getEmail(), userLoginDto.getPassword())
                .orElse(null);

        if (user == null) {
            System.out.println("Incorrect username / password");
            return;
        }

        loggedInUser = user;
    }

    @Override
    public void logoutUser() {

        if (loggedInUser == null) {
            System.out.println("Cannot log out. No user was logged in.");
        } else {
            loggedInUser = null;
        }
    }

    @Override
    public List<GameOwnedDto> findOwnedGames() {

       return userRepository.findAllByUser(loggedInUser.getId())
               .stream().map(game -> modelMapper.map(game, GameOwnedDto.class))
               .collect(Collectors.toList());
    }
}

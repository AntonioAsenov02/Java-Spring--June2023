package com.example.jsonexercise.service.impl;

import com.example.jsonexercise.model.dto.UserSeedDto;
import com.example.jsonexercise.model.dto.UserSoldDto;
import com.example.jsonexercise.model.entity.User;
import com.example.jsonexercise.repository.UserRepository;
import com.example.jsonexercise.service.UserService;
import com.example.jsonexercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.example.jsonexercise.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class UserServiceImpl implements UserService {

    private static final String USERS_FILE_NAME = "users.json";
    private final UserRepository userRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public void seedUsers() throws IOException {

        if (userRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + USERS_FILE_NAME));

        Arrays.stream(gson.fromJson(fileContent, UserSeedDto[].class))
                .filter(validationUtil::isValid)
                .map(userSeedDto -> modelMapper.map(userSeedDto, User.class))
                .forEach(userRepository::save);
    }

    @Override
    public User findRandomUser() {

        long randomId = ThreadLocalRandom
                .current().nextLong(1, userRepository.count() + 1);

        return userRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<UserSoldDto> findAllUsersWithMoreThanOneProductSold() {

        return userRepository.findAllWithAtLeastOneProductSoldOrderByLastNameAndFirstName()
                .stream()
                .map(user -> modelMapper.map(user, UserSoldDto.class))
                .collect(Collectors.toList());
    }
}

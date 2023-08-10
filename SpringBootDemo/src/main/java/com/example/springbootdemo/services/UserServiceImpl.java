package com.example.springbootdemo.services;

import com.example.springbootdemo.models.Account;
import com.example.springbootdemo.models.User;
import com.example.springbootdemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(String username, int age) {

        if (username.isBlank() || age < 18){
            throw new RuntimeException("Name or age is invalid");
        }

        Optional<User> byUsername = this.userRepository.findByUsername(username);
        if (byUsername.isPresent()){
            throw new RuntimeException("User already exists");
        }

        Account account = new Account();
        User user = new User(username, age, account);

        this.userRepository.save(user);
    }
}

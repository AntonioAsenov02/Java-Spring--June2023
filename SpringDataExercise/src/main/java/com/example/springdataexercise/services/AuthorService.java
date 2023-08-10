package com.example.springdataexercise.services;

import com.example.springdataexercise.models.entities.Author;

import java.io.IOException;
import java.util.List;

public interface AuthorService {
    void seedAuthors() throws IOException;

    Author getRandomAuthor();

    List<String> findAllAuthorsOrderedByNumberOfBooksDesc();
}

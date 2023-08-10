package com.example.springdataexercise.services;

import com.example.springdataexercise.models.entities.Book;

import java.io.IOException;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBookWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastName(String firstName, String lastName);
}

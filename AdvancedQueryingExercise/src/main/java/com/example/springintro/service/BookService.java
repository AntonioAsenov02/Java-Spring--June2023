package com.example.springintro.service;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface BookService {
    void seedBooks() throws IOException;

    List<Book> findAllBooksAfterYear(int year);

    List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year);

    List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName);

    List<String> findAllBookTitlesWithAgeRestriction(AgeRestriction ageRestriction);

    List<String> goldenBooksWithLessThan5000Copies();

    List<String> booksByPrice();

    List<String> booksNotReleasedInAGivenYear(int year);

    List<String> booksReleasedBeforeGivenDate(LocalDate date);

    List<String> booksSearch(String containsString);

    List<String> booksTitlesSearch(String startsWith);

    int countBooksWithTitleLongerThan(int number);


    List<String> informationAboutBookByTitle(String bookTitle);

    int increaseBookCopiesWithReleaseDateAfter(LocalDate date, int copies);
}

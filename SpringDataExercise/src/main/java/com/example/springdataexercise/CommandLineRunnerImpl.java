package com.example.springdataexercise;

import com.example.springdataexercise.models.entities.Book;
import com.example.springdataexercise.services.AuthorService;
import com.example.springdataexercise.services.BookService;
import com.example.springdataexercise.services.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

//        printAllBooksReleaseDateAfter2000(2000);
//        printAllAuthorsWithBooksWithReleaseDateBeforeYear(1990);
//        printAllAuthorsAndTheirBookCount();

        printAllBooksByAuthorOrderedByReleaseDateDesc("George", "Powell");
    }

    private void printAllBooksByAuthorOrderedByReleaseDateDesc(String firstName, String lastName) {
        bookService.findAllBooksByAuthorFirstAndLastName(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndTheirBookCount() {
        authorService.findAllAuthorsOrderedByNumberOfBooksDesc()
                .forEach(System.out::println);
    }

    private void printAllAuthorsWithBooksWithReleaseDateBeforeYear(int year) {
        bookService.findAllAuthorsWithBookWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksReleaseDateAfter2000(int year) {

        bookService.findAllBooksAfterYear(year)
                .stream()
                .map(Book::getTitle)
                .forEach(System.out::println);
    }

    private void seedData() throws IOException {
        categoryService.seedCategories();
        authorService.seedAuthors();
        bookService.seedBooks();
    }
}

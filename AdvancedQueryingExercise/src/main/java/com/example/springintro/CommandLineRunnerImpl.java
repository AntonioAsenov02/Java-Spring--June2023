package com.example.springintro;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private final CategoryService categoryService;
    private final AuthorService authorService;
    private final BookService bookService;
    private final BufferedReader bufferedReader;

    public CommandLineRunnerImpl(CategoryService categoryService, AuthorService authorService, BookService bookService, BufferedReader bufferedReader) {
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.bookService = bookService;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        seedData();

//        printAllBooksAfterYear(2000);
//        printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(1990);
//        printAllAuthorsAndNumberOfTheirBooks();
//        printALlBooksByAuthorNameOrderByReleaseDate("George", "Powell");

        System.out.println("Select exercise number: ");
        int exerciseNumber = Integer.parseInt(bufferedReader.readLine());

        switch (exerciseNumber) {
            case 1 -> booksTitlesByAgeRestriction();
            case 2 -> goldenEditionBookTitlesWithCopiesLessThan5000();
            case 3 -> booksByPrice();
            case 4 -> booksNotReleasedInAGivenYear();
            case 5 -> booksReleasedBeforeGivenDate();
            case 6 -> authorsSearch();
            case 7 -> booksSearch();
            case 8 -> booksTitlesSearch();
            case 9 -> countBooks();
            case 10 -> totalBookCopies();
            case 11 -> reducedBook();
            case 12 -> increaseBookCopies();
        }

    }

    private void increaseBookCopies() throws IOException {

        System.out.println("Enter date: ");

        LocalDate date = LocalDate.parse(bufferedReader.readLine(), DateTimeFormatter.ofPattern("dd MMM yyyy"));

        System.out.println("Enter copies number: ");

        int copies = Integer.parseInt(bufferedReader.readLine());

        System.out.println(bookService.increaseBookCopiesWithReleaseDateAfter(date, copies));
    }

    private void reducedBook() throws IOException {

        System.out.println("Enter book title: ");

        String bookTitle = bufferedReader.readLine();

        bookService.informationAboutBookByTitle(bookTitle)
                .forEach(System.out::println);
    }

    private void totalBookCopies() {

        authorService.totalNumberOfCopiesByAuthor()
                .forEach(System.out::println);
    }

    private void countBooks() throws IOException {

        System.out.println("Enter number: ");

        int number = Integer.parseInt(bufferedReader.readLine());

        System.out.println(bookService.countBooksWithTitleLongerThan(number));

    }

    private void booksTitlesSearch() throws IOException {

        System.out.println("Enter lastName starting string: ");

        String startsWith = bufferedReader.readLine();

        bookService.booksTitlesSearch(startsWith)
                .forEach(System.out::println);
    }

    private void booksSearch() throws IOException {

        System.out.println("Enter string: ");

        String containsString = bufferedReader.readLine();

        bookService.booksSearch(containsString)
                .forEach(System.out::println);
    }

    private void authorsSearch() throws IOException {

        System.out.println("Enter string: ");

        String endsWith = bufferedReader.readLine();

        authorService.authorsSearch(endsWith)
                .forEach(System.out::println);
    }

    private void booksReleasedBeforeGivenDate() throws IOException {

        System.out.println("Enter date: ");

        LocalDate date = LocalDate.parse(bufferedReader.readLine(), DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        bookService.booksReleasedBeforeGivenDate(date)
                .forEach(System.out::println);
    }

    private void booksNotReleasedInAGivenYear() throws IOException {

        System.out.println("Enter year: ");

        int year = Integer.parseInt(bufferedReader.readLine());

        bookService.booksNotReleasedInAGivenYear(year)
                .forEach(System.out::println);
    }

    private void booksByPrice() {

        bookService.booksByPrice()
                .forEach(System.out::println);
    }

    private void goldenEditionBookTitlesWithCopiesLessThan5000() {

        bookService.goldenBooksWithLessThan5000Copies()
                .forEach(System.out::println);
    }


    private void booksTitlesByAgeRestriction() throws IOException {

        System.out.println("Enter age restriction: ");

        AgeRestriction ageRestriction = AgeRestriction.valueOf(bufferedReader.readLine().toUpperCase());

        bookService.findAllBookTitlesWithAgeRestriction(ageRestriction)
                .forEach(System.out::println);
    }

    private void printALlBooksByAuthorNameOrderByReleaseDate(String firstName, String lastName) {
        bookService
                .findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(firstName, lastName)
                .forEach(System.out::println);
    }

    private void printAllAuthorsAndNumberOfTheirBooks() {
        authorService
                .getAllAuthorsOrderByCountOfTheirBooks()
                .forEach(System.out::println);
    }

    private void printAllAuthorsNamesWithBooksWithReleaseDateBeforeYear(int year) {
        bookService
                .findAllAuthorsWithBooksWithReleaseDateBeforeYear(year)
                .forEach(System.out::println);
    }

    private void printAllBooksAfterYear(int year) {
        bookService
                .findAllBooksAfterYear(year)
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

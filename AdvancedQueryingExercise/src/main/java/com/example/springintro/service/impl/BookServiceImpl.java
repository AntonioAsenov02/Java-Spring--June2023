package com.example.springintro.service.impl;

import com.example.springintro.model.entity.*;
import com.example.springintro.repository.BookRepository;
import com.example.springintro.service.AuthorService;
import com.example.springintro.service.BookService;
import com.example.springintro.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private static final String BOOKS_FILE_PATH = "src/main/resources/files/books.txt";

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService, CategoryService categoryService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedBooks() throws IOException {
        if (bookRepository.count() > 0) {
            return;
        }

        Files
                .readAllLines(Path.of(BOOKS_FILE_PATH))
                .forEach(row -> {
                    String[] bookInfo = row.split("\\s+");

                    Book book = createBookFromInfo(bookInfo);

                    bookRepository.save(book);
                });
    }

    @Override
    public List<Book> findAllBooksAfterYear(int year) {
        return bookRepository
                .findAllByReleaseDateAfter(LocalDate.of(year, 12, 31));
    }

    @Override
    public List<String> findAllAuthorsWithBooksWithReleaseDateBeforeYear(int year) {
        return bookRepository
                .findAllByReleaseDateBefore(LocalDate.of(year, 1, 1))
                .stream()
                .map(book -> String.format("%s %s", book.getAuthor().getFirstName(),
                        book.getAuthor().getLastName()))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBooksByAuthorFirstAndLastNameOrderByReleaseDate(String firstName, String lastName) {
       return bookRepository
                .findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(firstName, lastName)
                .stream()
                .map(book -> String.format("%s %s %d",
                        book.getTitle(),
                        book.getReleaseDate(),
                        book.getCopies()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findAllBookTitlesWithAgeRestriction(AgeRestriction ageRestriction) {

        return bookRepository.findAllByAgeRestriction(ageRestriction)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> goldenBooksWithLessThan5000Copies() {
        return bookRepository.findAllByEditionTypeAndCopiesLessThan(EditionType.GOLD, 5000)
                .stream().map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> booksByPrice() {
        return bookRepository
                .findAllByPriceLessThanOrPriceGreaterThan(new BigDecimal(5), new BigDecimal(40))
                .stream()
                .map(b -> String.format("%s - $%.2f", b.getTitle(), b.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> booksNotReleasedInAGivenYear(int year) {

        LocalDate beforeDate = LocalDate.of(year, 1, 1);
        LocalDate afterDate = LocalDate.of(year, 12, 31);

        return bookRepository.findAllByReleaseDateBeforeOrReleaseDateAfter(
                beforeDate, afterDate)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> booksReleasedBeforeGivenDate(LocalDate date) {
        return bookRepository.findAllByReleaseDateBefore(date)
                .stream()
                .map(b -> String.format("%s %s %.2f", b.getTitle(), b.getEditionType(), b.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> booksSearch(String containsString) {
        return bookRepository.findByTitleContainingIgnoreCase(containsString)
                .stream()
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> booksTitlesSearch(String startsWith) {
        return bookRepository.findAllByAuthor_LastNameStartingWith(startsWith)
                .stream()
                .map(b -> String.format("%s (%s %s)", b.getTitle(),
                        b.getAuthor().getFirstName(), b.getAuthor().getLastName()))
                .collect(Collectors.toList());
    }

    @Override
    public int countBooksWithTitleLongerThan(int number) {
        return bookRepository.countBooksByTitleLengthLongerThanGiven(number);
    }

    @Override
    public List<String> informationAboutBookByTitle(String bookTitle) {
        return bookRepository.findByTitle(bookTitle)
                .stream()
                .map(b -> String.format("%s %s %s %.2f", b.getTitle(),
                        b.getEditionType(), b.getAgeRestriction(), b.getPrice()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public int increaseBookCopiesWithReleaseDateAfter(LocalDate date, int copies) {

        int affectedRows = bookRepository.increaseCopiesWhereReleaseDateAfter(date, copies);

        return affectedRows * 100;
    }

    private Book createBookFromInfo(String[] bookInfo) {
        EditionType editionType = EditionType.values()[Integer.parseInt(bookInfo[0])];
        LocalDate releaseDate = LocalDate
                .parse(bookInfo[1], DateTimeFormatter.ofPattern("d/M/yyyy"));
        Integer copies = Integer.parseInt(bookInfo[2]);
        BigDecimal price = new BigDecimal(bookInfo[3]);
        AgeRestriction ageRestriction = AgeRestriction
                .values()[Integer.parseInt(bookInfo[4])];
        String title = Arrays.stream(bookInfo)
                .skip(5)
                .collect(Collectors.joining(" "));

        Author author = authorService.getRandomAuthor();
        Set<Category> categories = categoryService
                .getRandomCategories();

        return new Book(editionType, releaseDate, copies, price, ageRestriction, title, author, categories);

    }
}

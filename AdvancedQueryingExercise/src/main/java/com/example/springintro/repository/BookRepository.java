package com.example.springintro.repository;

import com.example.springintro.model.entity.AgeRestriction;
import com.example.springintro.model.entity.Book;
import com.example.springintro.model.entity.EditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAllByReleaseDateAfter(LocalDate releaseDateAfter);

    List<Book> findAllByReleaseDateBefore(LocalDate releaseDateBefore);

    List<Book> findAllByAuthor_FirstNameAndAuthor_LastNameOrderByReleaseDateDescTitle(String author_firstName, String author_lastName);

    List<Book> findAllByAgeRestriction(AgeRestriction ageRestriction);

    List<Book> findAllByEditionTypeAndCopiesLessThan(EditionType editionType, int copies);

    List<Book> findAllByPriceLessThanOrPriceGreaterThan(BigDecimal min, BigDecimal max);

    List<Book> findAllByReleaseDateBeforeOrReleaseDateAfter(LocalDate beforeDate, LocalDate afterDate);

    List<Book> findByTitleContainingIgnoreCase(String containing);

    List<Book> findAllByAuthor_LastNameStartingWith(String starsWith);

    @Query("SELECT COUNT(b.id) FROM Book b " +
            "WHERE length(b.title) > :number")
    int countBooksByTitleLengthLongerThanGiven(int number);

    List<Book> findByTitle(String title);

    @Modifying
    @Query("UPDATE Book b SET b.copies = b.copies + :copies " +
            "WHERE b.releaseDate > :releaseDate")
    int increaseCopiesWhereReleaseDateAfter(LocalDate releaseDate, int copies);
}

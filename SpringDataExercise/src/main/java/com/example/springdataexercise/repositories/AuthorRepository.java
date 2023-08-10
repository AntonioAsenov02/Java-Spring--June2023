package com.example.springdataexercise.repositories;

import com.example.springdataexercise.models.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a.firstName, a.lastName, count(ab.copies) AS bcopies FROM Author a JOIN a.books AS ab GROUP BY a.id ORDER BY bcopies DESC")
    List<String> findAllByBooksSizeDESC();
}

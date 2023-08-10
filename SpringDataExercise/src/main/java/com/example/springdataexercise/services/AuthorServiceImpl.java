package com.example.springdataexercise.services;

import com.example.springdataexercise.models.entities.Author;
import com.example.springdataexercise.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final static String AUTHOR_FILE_PATH = "src\\main\\resources\\files\\authors.txt";
    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void seedAuthors() throws IOException {

        if (authorRepository.count() > 0){
            return;
        }

        Files.readAllLines(Path.of(AUTHOR_FILE_PATH))
                .stream()
                .filter(row -> !row.isEmpty())
                .forEach(authorName -> {
                    String [] fullName = authorName.split("\\s+");
                    Author author = new Author(fullName[0], fullName[1]);

                    authorRepository.save(author);
                });
    }

    @Override
    public Author getRandomAuthor() {

        long randomId = ThreadLocalRandom.current()
                .nextLong(1, authorRepository.count() + 1);

        return authorRepository.findById(randomId).orElse(null);
    }

    @Override
    public List<String> findAllAuthorsOrderedByNumberOfBooksDesc() {

        return authorRepository.findAllByBooksSizeDESC()
                .stream()
                .map(row -> {
                    String[] values = row.split(",");
                    return String.format("%s %s - %d",values[0], values[1], Integer.parseInt(values[2]));
                })
                .collect(Collectors.toList());
    }
}

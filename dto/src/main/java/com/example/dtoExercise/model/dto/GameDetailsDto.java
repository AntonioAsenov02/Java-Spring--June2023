package com.example.dtoExercise.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GameDetailsDto {

    private String Title;
    private BigDecimal price;
    private String description;
    private LocalDate releaseDate;


    public GameDetailsDto() {

    }

    public GameDetailsDto(String title) {
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("Title: %s%n", getTitle()))
                .append(String.format("Price: %.2f%n", getPrice()))
                .append(String.format("Description: %s%n", getDescription()))
                .append(String.format("ReleaseDate: %s%n", getReleaseDate()));

        return builder.toString();
    }
}

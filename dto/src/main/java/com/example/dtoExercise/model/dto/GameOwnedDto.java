package com.example.dtoExercise.model.dto;

public class GameOwnedDto {

    private String title;

    public GameOwnedDto() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}

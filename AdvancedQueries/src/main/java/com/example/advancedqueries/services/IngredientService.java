package com.example.advancedqueries.services;

import com.example.advancedqueries.entities.Ingredient;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IngredientService {

    List<Ingredient> findByNameStartingWith(String startingWith);

    List<Ingredient> findByName(List<String> names);

    void deleteIngredientByName(String name);

    void updateIngredientsPrices();

    void updatePriceWithNamesGiven(List<String> names);
}

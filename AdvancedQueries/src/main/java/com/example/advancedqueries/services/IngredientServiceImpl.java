package com.example.advancedqueries.services;

import com.example.advancedqueries.entities.Ingredient;
import com.example.advancedqueries.repositories.IngredientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public List<Ingredient> findByNameStartingWith(String startingWith) {
        return ingredientRepository.findByNameStartingWith(startingWith);
    }

    @Override
    public List<Ingredient> findByName(List<String> names) {
        return ingredientRepository.findByNameInOrderByPrice(names);
    }

    @Override
    @Transactional
    public void deleteIngredientByName(String name) {
        ingredientRepository.deleteIngredientByName(name);
    }

    @Override
    @Transactional
    public void updateIngredientsPrices() {
        ingredientRepository.updateAllPrices();
    }

    @Override
    @Transactional
    public void updatePriceWithNamesGiven(List<String> names) {
        ingredientRepository.updatePriceWithNames(names);
    }
}

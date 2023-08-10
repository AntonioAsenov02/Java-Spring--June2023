package com.example.advancedqueries.services;

import com.example.advancedqueries.entities.Shampoo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ShampooService {

    List<Shampoo> findByBrand(String brand);

    List<Shampoo> findBySize(String size);

    List<Shampoo> findBySizeOrLabel(String size, Long labelId);

    List<Shampoo> findByPriceHigherThanGiven(BigDecimal price);

    int findCountByPriceLowerThanGiven(BigDecimal price);

    Set<Shampoo> getShampoosWithIngredients(Set<String> ingredients);

    List<Shampoo> countShampooIngredientsLessThan(int countGiven);
}

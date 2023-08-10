package com.example.advancedqueries.repositories;

import com.example.advancedqueries.entities.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    List<Ingredient> findByNameStartingWith(String startingWith);

    List<Ingredient> findByNameInOrderByPrice(List<String> names);

    void deleteIngredientByName(String name);

    @Query("UPDATE Ingredient AS i " +
            "SET i.price = i.price * 1.10")
    @Modifying
    void updateAllPrices();

    @Query("UPDATE Ingredient AS i " +
            "SET i.price = i.price * 1.10 " +
            "WHERE i.name IN :names")
    @Modifying
    void updatePriceWithNames(List<String> names);
}

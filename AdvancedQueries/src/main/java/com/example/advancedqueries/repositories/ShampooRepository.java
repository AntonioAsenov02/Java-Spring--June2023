package com.example.advancedqueries.repositories;

import com.example.advancedqueries.entities.Shampoo;
import com.example.advancedqueries.entities.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Repository
public interface ShampooRepository extends JpaRepository<Shampoo, Long> {

    List<Shampoo> findByBrand(String brand);

    List<Shampoo> findBySizeOrderById(Size size);

    List<Shampoo> findBySizeOrLabelIdOrderByPrice(Size size, Long labelId);

    List<Shampoo> findByPriceGreaterThanOrderByPriceDesc(BigDecimal price);

    List<Shampoo> getShampoosByPriceLessThan(BigDecimal price);

    @Query("SELECT s FROM Shampoo AS s " +
            "JOIN s.ingredients AS si " +
            "WHERE si.name IN :ingredients")
    Set<Shampoo> getShampoosWithIngredients(Set<String> ingredients);

    @Query("SELECT s AS count FROM Shampoo AS s " +
            "WHERE size(s.ingredients) < :countGiven")
    List<Shampoo> countShampooIngredientsLessThan(int countGiven);
}

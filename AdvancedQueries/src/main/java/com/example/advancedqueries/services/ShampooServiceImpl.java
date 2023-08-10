package com.example.advancedqueries.services;

import com.example.advancedqueries.entities.Shampoo;
import com.example.advancedqueries.entities.Size;
import com.example.advancedqueries.repositories.ShampooRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class ShampooServiceImpl implements ShampooService {

    private final ShampooRepository shampooRepository;

    public ShampooServiceImpl(ShampooRepository shampooRepository) {
        this.shampooRepository = shampooRepository;
    }


    @Override
    public List<Shampoo> findByBrand(String brand) {
        return shampooRepository.findByBrand(brand);
    }

    @Override
    public List<Shampoo> findBySize(String size) {
        return shampooRepository.findBySizeOrderById(Size.valueOf(size));
    }

    @Override
    public List<Shampoo> findBySizeOrLabel(String size, Long labelId) {
        return shampooRepository.findBySizeOrLabelIdOrderByPrice(Size.valueOf(size.toUpperCase()), labelId);
    }

    @Override
    public List<Shampoo> findByPriceHigherThanGiven(BigDecimal price) {
        return shampooRepository.findByPriceGreaterThanOrderByPriceDesc(price);
    }

    @Override
    public int findCountByPriceLowerThanGiven(BigDecimal price) {
        return shampooRepository.getShampoosByPriceLessThan(price).size();
    }

    @Override
    public Set<Shampoo> getShampoosWithIngredients(Set<String> ingredients) {
        return shampooRepository.getShampoosWithIngredients(ingredients);
    }

    @Override
    public List<Shampoo> countShampooIngredientsLessThan(int countGiven) {
        return shampooRepository.countShampooIngredientsLessThan(countGiven);
    }
}

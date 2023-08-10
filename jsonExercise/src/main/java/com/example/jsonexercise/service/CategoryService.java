package com.example.jsonexercise.service;

import com.example.jsonexercise.model.dto.CategoryByProductsCountDto;
import com.example.jsonexercise.model.entity.Category;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface CategoryService {
    void seedCategories() throws IOException;

    public Set<Category> findRandomCategories();

    List<CategoryByProductsCountDto> findCountOfProductsInCategory();
}

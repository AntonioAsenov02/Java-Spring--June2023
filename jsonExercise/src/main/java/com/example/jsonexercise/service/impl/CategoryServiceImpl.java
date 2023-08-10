package com.example.jsonexercise.service.impl;

import com.example.jsonexercise.model.dto.CategoryByProductsCountDto;
import com.example.jsonexercise.model.dto.CategorySeedDto;
import com.example.jsonexercise.model.entity.Category;
import com.example.jsonexercise.repository.CategoryRepository;
import com.example.jsonexercise.service.CategoryService;
import com.example.jsonexercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.jsonexercise.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final static String CATEGORIES_FILE_NAME = "categories.json";

    private final CategoryRepository categoryRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson) {
        this.categoryRepository = categoryRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
    }

    @Override
    public void seedCategories() throws IOException {

        if (categoryRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + CATEGORIES_FILE_NAME));

        CategorySeedDto[] categorySeedDtos =
                gson.fromJson(fileContent, CategorySeedDto[].class);


        Arrays.stream(categorySeedDtos)
                .filter(validationUtil::isValid)
                .map(categorySeedDto -> modelMapper.map(categorySeedDto, Category.class))
                .forEach(categoryRepository::save);
    }

    @Override
    public Set<Category> findRandomCategories() {

        Set<Category> categories = new HashSet<>();
        int categoriesCount = ThreadLocalRandom.current().nextInt(1,3);
        long totalCategoriesCount = categoryRepository.count() + 1;

        for (int i = 0; i < categoriesCount; i++) {

            long randomId = ThreadLocalRandom
                    .current().nextLong(1, totalCategoriesCount);

            categories.add(categoryRepository.findById(randomId).orElse(null));
        }

        return categories;
    }

    @Override
    public List<CategoryByProductsCountDto> findCountOfProductsInCategory() {

        return categoryRepository.findCountOfProductsByCategory().orElse(null);
    }
}

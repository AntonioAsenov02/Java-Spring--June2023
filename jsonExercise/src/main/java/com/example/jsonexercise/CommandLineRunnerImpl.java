package com.example.jsonexercise;

import com.example.jsonexercise.model.dto.CategoryByProductsCountDto;
import com.example.jsonexercise.model.dto.ProductNameAndPriceDto;
import com.example.jsonexercise.model.dto.UserSoldDto;
import com.example.jsonexercise.service.CategoryService;
import com.example.jsonexercise.service.ProductService;
import com.example.jsonexercise.service.UserService;
import com.google.gson.Gson;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String OUT_PATH = "src/main/resources/files/out/";
    private static final String PRODUCTS_IN_RANGE_FILE_NAME = "products-in-range.json";
    private static final String USERS_AND_SOLD_PRODUCTS = "users-sold-products.json";
    private static final String CATEGORIES_BY_PRODUCTS = "categories-by-products.json";

    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;
    private final Gson gson;

    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService, Gson gson) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.gson = gson;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {

        seedDate();

        System.out.println("Enter exercise number: ");
        int exerciseNumber = Integer.parseInt(bufferedReader.readLine());

        switch (exerciseNumber) {
            case 1 -> productsInRange();
            case 2 -> soldProducts();
            case 3 -> categoriesByProductsCount();
        }
    }

    private void categoriesByProductsCount() throws IOException {

        List<CategoryByProductsCountDto> categoryByProductsCountDtos =
                categoryService.findCountOfProductsInCategory();

        String content = gson.toJson(categoryByProductsCountDtos);

        writeToFile(OUT_PATH + CATEGORIES_BY_PRODUCTS, content);
    }

    private void soldProducts() throws IOException {

        List<UserSoldDto> userSoldDtos =
                userService.findAllUsersWithMoreThanOneProductSold();

        String content = gson.toJson(userSoldDtos);

        writeToFile(OUT_PATH + USERS_AND_SOLD_PRODUCTS, content);
    }

    private void productsInRange() throws IOException {

        List<ProductNameAndPriceDto> productDtos =
                productService.findAllProductsInRangeOrderedByPrice(
                        BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L));


        String content = gson.toJson(productDtos);

        writeToFile(OUT_PATH + PRODUCTS_IN_RANGE_FILE_NAME, content);

    }

    private void writeToFile(String filePath, String content) throws IOException {

        Files.write(Path.of(filePath),
                Collections.singleton(content));
    }

    private void seedDate() throws IOException {

        categoryService.seedCategories();
        userService.seedUsers();
        productService.seedProducts();
    }
}

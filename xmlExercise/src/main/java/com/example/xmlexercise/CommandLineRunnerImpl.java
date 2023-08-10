package com.example.xmlexercise;

import com.example.xmlexercise.model.dto.*;
import com.example.xmlexercise.service.CategoryService;
import com.example.xmlexercise.service.ProductService;
import com.example.xmlexercise.service.UserService;
import com.example.xmlexercise.util.XmlParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String RESOURCES_FILE_PATH = "src/main/resources/files/";
    private static final String CATEGORY_FILE_NAME = "categories.xml";
    private static final String USER_FILE_NAME = "users.xml";
    private static final String PRODUCT_FILE_NAME = "products.xml";
    private static final String OUTPUT_FILE_PATH = "src/main/resources/files/out/";
    private static final String PRODUCTS_IN_RANGE_FiLE_NAME = "products-in-range.xml";
    private static final String USERS_WITH_PRODUCTS_SOLD = "users-sold-products.xml";
    private static final String CATEGORIES_BY_PRODUCTS_COUNT = "categories-by-products.xml";
    private final XmlParser xmlParser;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader bufferedReader;


    public CommandLineRunnerImpl(XmlParser xmlParser, CategoryService categoryService, UserService userService, ProductService productService) {
        this.xmlParser = xmlParser;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {

        seedData();

        System.out.println("Enter exercise number: ");
        int exerciseNumber = Integer.parseInt(bufferedReader.readLine());

        switch (exerciseNumber) {
            case 1 -> productsInRange();
            case 2 -> successfullySoldProducts();
            case 3 -> categoriesByProductsCount();
        }
    }

    private void categoriesByProductsCount() throws JAXBException {

        CategoryProductsRootDto rootDto =
                categoryService.findCategoriesByProductsCount();

        xmlParser.writeToFile(OUTPUT_FILE_PATH + CATEGORIES_BY_PRODUCTS_COUNT, rootDto);
    }

    private void successfullySoldProducts() throws JAXBException {

        UserViewRootDto rootDto = userService.findUsersWithAtLeastOneProductSold();

        xmlParser.writeToFile(OUTPUT_FILE_PATH + USERS_WITH_PRODUCTS_SOLD, rootDto);

        System.out.println(rootDto);
    }

    private void productsInRange() throws JAXBException {

        ProductViewRootDto rootDto = productService.findProductsInRangeWithNoBuyer();

        xmlParser.writeToFile(OUTPUT_FILE_PATH + PRODUCTS_IN_RANGE_FiLE_NAME, rootDto);

    }

    private void seedData() throws JAXBException, FileNotFoundException {

        if (categoryService.getEntityCount() == 0) {

            CategorySeedRootDto categorySeedRootDto =
                    xmlParser.fromFile(RESOURCES_FILE_PATH + CATEGORY_FILE_NAME,
                            CategorySeedRootDto.class);

            categoryService.seedCategories(categorySeedRootDto.getCategories());
        }

        if (userService.getEntityCount() == 0) {

            UserSeedRootDto userSeedRootDto = xmlParser.fromFile(RESOURCES_FILE_PATH + USER_FILE_NAME,
                    UserSeedRootDto.class);

            userService.seedUsers(userSeedRootDto.getUsers());
        }


        if (productService.getEntityCount()== 0) {

            ProductSeedRootDto productSeedRootDto =
                    xmlParser.fromFile(RESOURCES_FILE_PATH + PRODUCT_FILE_NAME,
                    ProductSeedRootDto.class);

            productService.seedProduct(productSeedRootDto.getProducts());
        }

    }
}

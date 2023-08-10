package com.example.jsonexercise.service.impl;

import com.example.jsonexercise.model.dto.ProductNameAndPriceDto;
import com.example.jsonexercise.model.dto.ProductSeedDto;
import com.example.jsonexercise.model.entity.Product;
import com.example.jsonexercise.repository.ProductRepository;
import com.example.jsonexercise.service.CategoryService;
import com.example.jsonexercise.service.ProductService;
import com.example.jsonexercise.service.UserService;
import com.example.jsonexercise.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.jsonexercise.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String PRODUCT_FILE_NAME = "products.json";
    private final ProductRepository productRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedProducts() throws IOException {

        if (productRepository.count() > 0) {
            return;
        }

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + PRODUCT_FILE_NAME));

        ProductSeedDto[] productSeedDtos =
                gson.fromJson(fileContent, ProductSeedDto[].class);


        Arrays.stream(productSeedDtos)
                .filter(validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                    product.setSeller(userService.findRandomUser());
                    if (product.getPrice().compareTo(BigDecimal.valueOf(800)) > 0) {
                        product.setBuyer(userService.findRandomUser());
                    }

                    product.setCategories(categoryService.findRandomCategories());

                    return product;
                })
                .forEach(productRepository::save);
    }

    @Override
    public List<ProductNameAndPriceDto> findAllProductsInRangeOrderedByPrice(BigDecimal min, BigDecimal max) {

        return productRepository.findAllByPriceBetweenAndBuyerIsNullOrderByPrice(min, max)
                .stream()
                .map(product -> {
                    ProductNameAndPriceDto productNameAndPriceDto =
                            modelMapper.map(product, ProductNameAndPriceDto.class);

                    productNameAndPriceDto.setName(String.format("%s %s",
                            product.getSeller().getFirstName(),
                            product.getSeller().getLastName()));

                    return  productNameAndPriceDto;
                })
                .collect(Collectors.toList());

    }
}

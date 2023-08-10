package com.example.jsonexercise.service;

import com.example.jsonexercise.model.dto.ProductNameAndPriceDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    void seedProducts() throws IOException;

    List<ProductNameAndPriceDto> findAllProductsInRangeOrderedByPrice(BigDecimal min, BigDecimal max);


}

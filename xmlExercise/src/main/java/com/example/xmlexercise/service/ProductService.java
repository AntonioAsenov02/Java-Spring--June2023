package com.example.xmlexercise.service;

import com.example.xmlexercise.model.dto.ProductSeedDto;
import com.example.xmlexercise.model.dto.ProductViewRootDto;

import java.util.List;

public interface ProductService {
    long getEntityCount();

    void seedProduct(List<ProductSeedDto> products);

    ProductViewRootDto findProductsInRangeWithNoBuyer();
}

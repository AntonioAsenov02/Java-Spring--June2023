package com.example.jsonexercise.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CategoryByProductsCountDto {

    @Expose()
    @SerializedName("category")
    private String name;
    @Expose
    @SerializedName("productsCount")
    private Long countOfProducts;
    @Expose
    @SerializedName("averagePrice")
    private Double averagePrice;
    @Expose
    @SerializedName("totalRevenue")
    private BigDecimal totalRevenue;

    public CategoryByProductsCountDto() {

    }

    public CategoryByProductsCountDto(String name, Long countOfProducts, Double averagePrice, BigDecimal totalRevenue) {
        this.name = name;
        this.countOfProducts = countOfProducts;
        this.averagePrice = averagePrice;
        this.totalRevenue = totalRevenue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCountOfProducts() {
        return countOfProducts;
    }

    public void setCountOfProducts(Long countOfProducts) {
        this.countOfProducts = countOfProducts;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}

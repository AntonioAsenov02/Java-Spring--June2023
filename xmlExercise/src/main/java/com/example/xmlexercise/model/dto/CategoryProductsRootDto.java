package com.example.xmlexercise.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryProductsRootDto {

    @XmlElement(name = "category")
    private List<CategoryWithProductsDto> categories;

    public List<CategoryWithProductsDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryWithProductsDto> categories) {
        this.categories = categories;
    }
}

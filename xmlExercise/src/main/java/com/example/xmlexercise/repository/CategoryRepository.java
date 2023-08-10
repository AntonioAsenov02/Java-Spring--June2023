package com.example.xmlexercise.repository;

import com.example.xmlexercise.model.dto.CategoryWithProductsDto;
import com.example.xmlexercise.model.entity.Category;
import jakarta.persistence.NamedNativeQueries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT new com.example.xmlexercise.model.dto.CategoryWithProductsDto" +
            "(c.name, COUNT(p.id), AVG(p.price), SUM(p.price))" +
            "FROM Product p " +
            "JOIN p.categories c " +
            "GROUP BY c.id " +
            "ORDER BY count(p.id)")
    Optional<List<CategoryWithProductsDto>> findCategoriesByByProductsCount();
}

package com.iftm.curso.repositories;

import com.iftm.curso.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.iftm.curso.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>{
    @Transactional(readOnly = true)
    @Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats  WHERE LOWER(obj.name) LIKE LOWER(CONCAT( '%', :name, '%')) AND cats IN :categories")
    Page<Product> findByNameContainingIgnoreCaseAndCategoriesIn(String name, List<Category> categories, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT obj FROM Product obj WHERE LOWER(obj.name) LIKE LOWER(CONCAT( '%', :name, '%'))")
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("SELECT obj FROM Product obj  INNER JOIN obj.categories cats WHERE :category IN cats")
    Page<Product> findByCategory(Category category, Pageable pageable);
}

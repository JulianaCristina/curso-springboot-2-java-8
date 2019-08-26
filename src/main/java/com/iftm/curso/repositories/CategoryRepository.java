package com.iftm.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iftm.curso.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

}

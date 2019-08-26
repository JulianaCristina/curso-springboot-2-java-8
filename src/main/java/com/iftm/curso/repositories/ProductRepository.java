package com.iftm.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iftm.curso.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}

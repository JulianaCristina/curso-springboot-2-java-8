package com.iftm.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iftm.curso.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}

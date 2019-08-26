package com.iftm.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iftm.curso.entities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}

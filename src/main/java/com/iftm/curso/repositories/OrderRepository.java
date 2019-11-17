package com.iftm.curso.repositories;

import com.iftm.curso.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.iftm.curso.entities.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByClient(User client);
}

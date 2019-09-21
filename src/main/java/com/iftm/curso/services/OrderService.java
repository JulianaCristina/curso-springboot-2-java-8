package com.iftm.curso.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.iftm.curso.dto.OrderDTO;
import com.iftm.curso.dto.UserDTO;
import com.iftm.curso.entities.User;
import com.iftm.curso.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iftm.curso.entities.Order;
import com.iftm.curso.repositories.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
	
	@Autowired
	private OrderRepository repository;

	public List<OrderDTO> findAll(){

		List<Order> list = repository.findAll();

		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}

	public OrderDTO findById(Long id) {
		Optional<Order> obj = repository.findById(id);
		Order entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));

		return new OrderDTO(entity);
	}
}

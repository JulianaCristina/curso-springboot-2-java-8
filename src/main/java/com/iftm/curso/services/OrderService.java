package com.iftm.curso.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.iftm.curso.dto.OrderDTO;
import com.iftm.curso.dto.OrderItemDTO;
import com.iftm.curso.dto.UserDTO;
import com.iftm.curso.entities.OrderItem;
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

	@Autowired
	private AuthService authService;

	public List<OrderDTO> findAll(){

		List<Order> list = repository.findAll();

		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}

	public OrderDTO findById(Long id) {
		Optional<Order> obj = repository.findById(id);
		Order entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		authService.validateOwnOrderOrAdmin(entity);
		return new OrderDTO(entity);
	}

	public List<OrderDTO> findByClient(){
		User client = authService.authenticated();
		List<Order> list = repository.findByClient(client);
		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
    public List<OrderItemDTO> findItems(Long id) {
		 Order order = repository.getOne(id);
		authService.validateOwnOrderOrAdmin(order);
		Set<OrderItem> set = order.getItems();
		return set.stream().map(e -> new OrderItemDTO(e)).collect(Collectors.toList());

	}
}

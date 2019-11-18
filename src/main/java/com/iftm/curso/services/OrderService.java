package com.iftm.curso.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.iftm.curso.dto.OrderDTO;
import com.iftm.curso.dto.OrderItemDTO;
import com.iftm.curso.dto.UserDTO;
import com.iftm.curso.entities.OrderItem;
import com.iftm.curso.entities.Product;
import com.iftm.curso.entities.User;
import com.iftm.curso.entities.enums.OrderStatus;
import com.iftm.curso.repositories.OrderItemRepository;
import com.iftm.curso.repositories.ProductRepository;
import com.iftm.curso.repositories.UserRepository;
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

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

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
	@Transactional(readOnly = true)
	public List<OrderDTO> findByClientId(Long clientId) {
		User client = userRepository.getOne(clientId);
		List<Order> list = repository.findByClient(client);
		return list.stream().map(e -> new OrderDTO(e)).collect(Collectors.toList());
	}

	@Transactional
    public OrderDTO placeOrder(List<OrderItemDTO> dto) {
		User client = authService.authenticated();
		Order order = new Order(null, Instant.now(), OrderStatus.WAITING_PAYMENT, client);

		for (OrderItemDTO itemDTO : dto){
			Product product = productRepository.getOne(itemDTO.getProductId());
			OrderItem item = new OrderItem(order, product, itemDTO.getQuantity(), itemDTO.getPrice());
			order.getItems().add(item);
		}
		repository.save(order);
		orderItemRepository.saveAll(order.getItems());

		return new OrderDTO(order);
	}
}

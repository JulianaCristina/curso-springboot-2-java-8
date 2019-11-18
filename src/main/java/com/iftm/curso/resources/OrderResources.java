package com.iftm.curso.resources;

import java.net.URI;
import java.util.List;

import com.iftm.curso.dto.OrderDTO;
import com.iftm.curso.dto.OrderItemDTO;
import com.iftm.curso.entities.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.iftm.curso.entities.Order;
import com.iftm.curso.services.OrderService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value =  "/orders")
public class OrderResources {
	
	@Autowired
	private OrderService service;

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<OrderDTO>> findAll(){
		List<OrderDTO> list = service.findAll();
		return ResponseEntity.ok().body(list );
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<OrderDTO> findById(@PathVariable Long id){
		OrderDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}

	@GetMapping(value = "/{id}/items")
	public ResponseEntity<List<OrderItemDTO>> findItems(@PathVariable Long id){
		List<OrderItemDTO> list = service.findItems(id);
		return ResponseEntity.ok().body(list);
	}


	@GetMapping(value = "/myorders")
	public ResponseEntity<List<OrderDTO>> findByClient(){
		List<OrderDTO> list = service.findByClient();
		return ResponseEntity.ok().body(list );
	}
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping(value = "/client/{clientId}")
	public ResponseEntity<List<OrderDTO>> findByClientId(@PathVariable Long clientId){
		List<OrderDTO> list = service.findByClientId(clientId);
		return ResponseEntity.ok().body(list );
	}
	@PostMapping
	public ResponseEntity<OrderDTO> placeOrder(@RequestBody List<OrderItemDTO> dto){
		OrderDTO orderDTO = service.placeOrder(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(orderDTO.getId()).toUri();

		return ResponseEntity.created(uri).body(orderDTO);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<OrderDTO> update(@PathVariable Long id,@RequestBody OrderDTO dto){
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}

} 

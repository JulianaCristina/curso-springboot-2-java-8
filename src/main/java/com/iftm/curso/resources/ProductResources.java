package com.iftm.curso.resources;

import java.net.URI;
import java.util.List;

import com.iftm.curso.dto.ProductCategoriesDTO;
import com.iftm.curso.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.iftm.curso.entities.Product;
import com.iftm.curso.services.ProductService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping(value =  "/products")
public class ProductResources {
	
	@Autowired
	private ProductService service;
	
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAllPaged(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerpPage", defaultValue = "12") Integer linesPerPpage,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPpage, Sort.Direction.valueOf(direction), orderBy );
		Page<ProductDTO> list = service.findAllPaged(pageRequest);
		return ResponseEntity.ok().body(list );
	}

	@GetMapping(value = "/category/{categoryId}")
	public ResponseEntity<Page<ProductDTO>> findByCategoryPaged(
			@PathVariable Long categoryId,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerpPage", defaultValue = "12") Integer linesPerPpage,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPpage, Sort.Direction.valueOf(direction), orderBy );
		Page<ProductDTO> list = service.findByCategoryPaged(categoryId,pageRequest);
		return ResponseEntity.ok().body(list );
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
		ProductDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductCategoriesDTO dto){
		ProductDTO newDTO = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(newDTO);
	}
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id,@RequestBody ProductCategoriesDTO dto ){
		ProductDTO newdto = service.update(id, dto);
		return ResponseEntity.ok().body(newdto);
	}
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
} 

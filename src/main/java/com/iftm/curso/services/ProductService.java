package com.iftm.curso.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.iftm.curso.dto.CategoryDTO;
import com.iftm.curso.dto.ProductCategoriesDTO;
import com.iftm.curso.dto.ProductDTO;
import com.iftm.curso.entities.Category;
import com.iftm.curso.repositories.CategoryRepository;
import com.iftm.curso.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iftm.curso.entities.Product;
import com.iftm.curso.repositories.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;

	@Autowired
	private CategoryRepository categoryRepository;

	public List<ProductDTO> findAll() {
		List<Product> list = repository.findAll();
		return list.stream().map(e -> new ProductDTO(e)).collect(Collectors.toList());
	}

	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO insert(ProductCategoriesDTO dto){
		Product entity = dto.toEntity();
		setProductCategories(entity, dto.getCategories());
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}


	//ao atualizar o produto, posso querer atualizar as categorias dele
	@Transactional
	public ProductDTO update(Long id, ProductCategoriesDTO dto){
		try{
			Product entity = repository.getOne(id); //Instancio um usuario baseado no id usando getOne
			updateData(entity, dto); //atualizo os dados do usuario com base nos dto enviados na requisição
			entity = repository.save(entity); //salvo no banco
			return new ProductDTO(entity); //converto
		}catch(EntityNotFoundException e){
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Product entity, ProductCategoriesDTO dto) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());

		if (dto.getCategories() != null && dto.getCategories().size() > 0){
			setProductCategories(entity, dto.getCategories());
		}
	}

	private void setProductCategories(Product entity, List<CategoryDTO> categories){
		entity.getCategories().clear();
		for (CategoryDTO dto : categories){
			Category category = categoryRepository.getOne(dto.getId());
			entity.getCategories().add(category);
		}
	}

}

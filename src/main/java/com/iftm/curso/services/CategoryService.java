package com.iftm.curso.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.iftm.curso.dto.CategoryDTO;
import com.iftm.curso.dto.CategoryInsertDTO;
import com.iftm.curso.dto.UserDTO;
import com.iftm.curso.services.exceptions.DatabaseException;
import com.iftm.curso.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.iftm.curso.entities.Category;
import com.iftm.curso.repositories.CategoryRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.xml.crypto.Data;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	public List<CategoryDTO> findAll(){

		List<Category> list = repository.findAll();
		return list.stream().map(e -> new CategoryDTO(e)).collect(Collectors.toList());
	}
	
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));

		return new CategoryDTO(entity);
	}

	public CategoryDTO insert(CategoryInsertDTO dto){
		Category entity = dto.toEntity();
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}

	public void delete(Long id){
		try{
			repository.deleteById(id);
		}catch (EmptyResultDataAccessException e){
			throw new ResourceNotFoundException(id);
		}catch(DataIntegrityViolationException e){
			throw new DatabaseException(e.getMessage());
		}
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto){
		try{
			Category entity = repository.getOne(id);
			updateData(entity, dto);
			entity = repository.save(entity);
			return new CategoryDTO(entity);

		}catch (EntityNotFoundException e){
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Category entity, CategoryDTO dto){
		entity.setName(dto.getName());
	}
}

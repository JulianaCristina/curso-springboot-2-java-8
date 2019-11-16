package com.iftm.curso.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.iftm.curso.dto.UserDTO;
import com.iftm.curso.dto.UserInsertDTO;
import com.iftm.curso.services.exceptions.DatabaseException;
import com.iftm.curso.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.iftm.curso.entities.User;
import com.iftm.curso.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository repository;
	
	public List<UserDTO> findAll(){
		
		List<User> list = repository.findAll();

		return list.stream().map(e -> new UserDTO(e)).collect(Collectors.toList());
	}
	
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));
		return new UserDTO(entity);
	}

	public UserDTO insert(UserInsertDTO dto){
		User entity = dto.toEntity();
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	public void delete(Long id){
		try{
			repository.deleteById(id);
		}catch (EmptyResultDataAccessException e){
			throw new  ResourceNotFoundException(id);
		}catch (DataIntegrityViolationException e){
			throw new DatabaseException(e.getMessage());
		}
	}

	@Transactional
	public UserDTO update(Long id, UserDTO dto){
		try{
			User entity = repository.getOne(id); //Instancio um usuario baseado no id usando getOne
			updateData(entity, dto); //atualizo os dados do usuario com base nos dto enviados na requisição
			entity = repository.save(entity); //salvo no banco
			return new UserDTO(entity); //converto
		}catch(EntityNotFoundException e){
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(User entity, UserDTO dto) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setPhone(dto.getPhone());
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return user;
	}
}

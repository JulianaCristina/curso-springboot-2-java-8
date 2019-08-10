package com.iftm.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iftm.curso.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

}

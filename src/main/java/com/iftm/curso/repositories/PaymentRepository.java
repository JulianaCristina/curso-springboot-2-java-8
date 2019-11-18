package com.iftm.curso.repositories;

import com.iftm.curso.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}

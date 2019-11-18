package com.iftm.curso.services;

import com.iftm.curso.dto.PaymentDTO;
import com.iftm.curso.entities.Payment;
import com.iftm.curso.repositories.PaymentRepository;
import com.iftm.curso.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<PaymentDTO> findAll(){

        List<Payment> list = paymentRepository.findAll();
        return list.stream().map(e -> new PaymentDTO(e)).collect(Collectors.toList());
    }

    public PaymentDTO findById(Long id) {
        Optional<Payment> obj = paymentRepository.findById(id);
        Payment entity = obj.orElseThrow(() -> new ResourceNotFoundException(id));

        return new PaymentDTO(entity);
    }
}

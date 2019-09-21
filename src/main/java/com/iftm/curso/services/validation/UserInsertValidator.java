package com.iftm.curso.services.validation;

import com.iftm.curso.dto.UserInsertDTO;
import com.iftm.curso.entities.User;
import com.iftm.curso.repositories.UserRepository;
import com.iftm.curso.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    //fala se o objeto é valido ou não, vdd ok, falso encontrou um erro
    //
    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();//cria uma lista de field, que carrega os erros e as mensagens

        User user = userRepository.findByEmail(dto.getEmail());

        //testar se os dtos estão violando algo,
        if(user != null){
            list.add(new FieldMessage("email", "email already taken"));
        }

        //percorrer os objetos, ai add eles na variavel context
        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
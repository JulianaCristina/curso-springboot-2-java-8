package com.iftm.curso.services.validation;

import com.iftm.curso.dto.UserDTO;
import com.iftm.curso.dto.UserInsertDTO;
import com.iftm.curso.entities.User;
import com.iftm.curso.repositories.UserRepository;
import com.iftm.curso.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    //fala se o objeto é valido ou não, vdd ok, falso encontrou um erro
    //
    @Override
    public boolean isValid(UserDTO dto, ConstraintValidatorContext context) {

        @SuppressWarnings("unchecked")
        Map<String, String> map =
                (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long uriId = Long.parseLong(map.get("id"));

        List<FieldMessage> list = new ArrayList<>();//cria uma lista de field, que carrega os erros e as mensagens

        User user = userRepository.findByEmail(dto.getEmail());

        //testar se os dtos estão violando algo,
        if(user != null && !user.getId().equals(uriId)){
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
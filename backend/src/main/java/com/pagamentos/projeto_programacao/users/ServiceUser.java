package com.pagamentos.projeto_programacao.users;

import com.pagamentos.projeto_programacao.users.DTO.CreateUser;
import com.pagamentos.projeto_programacao.users.exceptions.ExceptionInputInvalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
public class ServiceUser {
    @Autowired
    private static RepositoryUser repositoryUser;

    @Transactional
    public void create (CreateUser body) {
        User user = new User();

        if(body.cpf() == null || body.dtBirth() == null || body.name() == null || body.email() == null || body.password() == null) {
            throw new ExceptionInputInvalid("Informações insuficientes, esperado: CPf, Data de nascimento, nome, email e senha");
        }

        if (!HelpersUser.checkCpf(body.cpf())) {
            throw new ExceptionInputInvalid("Cpf invalido");
        }

        if(!HelpersUser.checkEmail(body.email())){
            throw new ExceptionInputInvalid("Email invalido");
        }

        if(body.name().length() < 4) {
            throw  new ExceptionInputInvalid("Nome precisa ter mais de 3 caracteres");
        }

        String passwordHash;

        try {
            passwordHash = HelpersUser.createPasswordHash(body.password());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e.getMessage());
        }

        user.setEmail(body.email());
        user.setCpf(body.cpf());
        user.setPassword(passwordHash);
        user.setName(body.name());
        user.setDtBirth(body.dtBirth());

        repositoryUser.save(user);

    }



}

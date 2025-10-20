package com.pagamentos.projeto_programacao.users;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class ControllerUser {
    @PostMapping("/")
    public ResponseEntity<String> create (@RequestBody DTOUser userBody) {

        ModelUser user = new ModelUser();

        try {
            user.setCpf(userBody.cpf());
            user.setName(userBody.name());
            user.setDtBirth(userBody.dtBirth());
            user.setPassword(userBody.password());
            user.setEmail(userBody.email());
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

        return ResponseEntity.status(400).body("Acertou");

    }
}

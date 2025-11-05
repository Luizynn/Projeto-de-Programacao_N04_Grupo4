package com.pagamentos.projeto_programacao.users;

import com.pagamentos.projeto_programacao.response.Response;
import com.pagamentos.projeto_programacao.users.DTO.CreateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class ControllerUser {

    @Autowired
    static private ServiceUser serviceUser;

    @PostMapping("/")
    public ResponseEntity<Response<String>> create (@RequestBody CreateUser userBody) {
        Response<String> response = new Response<String>(HttpStatus.CREATED);

        response.setData("AEEE");
        serviceUser.create(userBody);

        return ResponseEntity.status(response.getStatus()).body(response);

    }
}

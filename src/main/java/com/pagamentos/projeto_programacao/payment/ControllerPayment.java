package com.pagamentos.projeto_programacao.payment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class ControllerPayment {

    @Autowired
    private ServicePayment servicePayment;
}

package com.pagamentos.projeto_programacao.payment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServicePayment {

    @Autowired
    private RepositoryPayment repositoryPayment;
}

package com.pagamentos.projeto_programacao.payment;


import com.pagamentos.projeto_programacao.event.DTO.ResponseEvent;
import com.pagamentos.projeto_programacao.event.Event;
import com.pagamentos.projeto_programacao.localization.Localization;
import com.pagamentos.projeto_programacao.payment.DTO.CreatePayment;
import com.pagamentos.projeto_programacao.payment.DTO.EditPayment;
import com.pagamentos.projeto_programacao.payment.DTO.ResponsePayment;
import com.pagamentos.projeto_programacao.users.RepositoryUser;
import com.pagamentos.projeto_programacao.users.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ServicePayment {

    @Autowired
    private RepositoryPayment repositoryPayment;

    @Autowired
    RepositoryUser repositoryUser;

    @Transactional
    public ResponsePayment create(CreatePayment body) {
        User client = repositoryUser
                .findById(body.id_client())
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente não existe")
                );

        Payment payment = new Payment();
        payment.setUser(client);
        payment.generatePayment(body.paymentMethod());
        payment.calculateTotal();


        Payment saved = repositoryPayment.save(payment);
        return new ResponsePayment(saved);
    }

    @Transactional
    public ResponsePayment editPayment(Long id, EditPayment body) {

        Payment payment = repositoryPayment.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado."));

        if (payment.getStatus().equals(PaymentStatus.PAID)) {
            throw new IllegalStateException("Não é possível editar um pagamento já pago.");
        }

        if (body.status() != null && !body.status().isBlank()) {
            try {
                PaymentStatus newStatus = PaymentStatus.valueOf(body.status().toUpperCase());
                payment.setStatus(newStatus);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Status inválido: " + body.status());
            }
        }


        if (body.paymentMethod() != null && !body.paymentMethod().isBlank()) {
            payment.setPaymentMethod(body.paymentMethod());
        }


        if (body.totalAmount() != null && body.totalAmount().compareTo(BigDecimal.ZERO) >= 0) {
            payment.setTotalAmount(body.totalAmount());
        }

        Payment updated = repositoryPayment.save(payment);
        return new ResponsePayment(updated);
    }

    @Transactional
    public List<ResponsePayment> findAll () {
        return repositoryPayment.findAll().stream().map(pay -> new ResponsePayment(pay)).toList();
    }

    @Transactional
    public List<ResponsePayment> findByClientId (Long idClient) {
        return repositoryPayment.findClientId(idClient).stream().map(pay -> new ResponsePayment(pay)).toList();
    }


}

package com.pagamentos.projeto_programacao.payment;


import com.pagamentos.projeto_programacao.event.Event;
import com.pagamentos.projeto_programacao.event.RepositoryEvent;
import com.pagamentos.projeto_programacao.payment.DTO.CreatePayment;
import com.pagamentos.projeto_programacao.payment.DTO.EditPayment;
import com.pagamentos.projeto_programacao.payment.DTO.ResponsePayment;
import com.pagamentos.projeto_programacao.users.RepositoryUser;
import com.pagamentos.projeto_programacao.users.User;
import com.pagamentos.projeto_programacao.voucher.RepositoryVoucher;
import com.pagamentos.projeto_programacao.voucher.Voucher;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePayment {

    @Autowired
    private RepositoryPayment repositoryPayment;

    @Autowired
    private RepositoryUser repositoryUser;

    @Autowired
    private RepositoryVoucher repositoryVoucher;

    @Autowired
    private RepositoryEvent repositoryEvent;

    @Transactional
    public ResponsePayment create(CreatePayment body) throws CardValidator.CardValidationException {


        if (body.paymentMethod() == null) {
            throw new IllegalArgumentException("Método de pagamento é obrigatório.");
        }
        PaymentMethod method = body.paymentMethod();
        if (method == PaymentMethod.CREDIT_CARD) {
            CardValidator.validate(
                    body.cardNumber(),
                    body.expirationMonth(),
                    body.expirationYear(),
                    body.cvv()
            );
        }


        User client = repositoryUser
                .findById(body.id_client())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não existe"));

        if (body.eventId() == null || body.eventId().isEmpty()) {
            throw new IllegalArgumentException("A lista de eventos não pode estar vazia.");
        }
        List<Event> eventsToPay = repositoryEvent.findAllById(body.eventId());

        if (eventsToPay.size() != body.eventId().size()) {
            throw new EntityNotFoundException("Um ou mais IDs de evento não foram encontrados.");
        }

        Voucher voucher = null;
        if (body.voucherCode() != null && !body.voucherCode().isBlank()) {
            voucher = repositoryVoucher.findByCupom(body.voucherCode())
                    .orElseThrow(() -> new EntityNotFoundException("Cupom de voucher inválido"));
        }

        Payment payment = new Payment();
        payment.setClient(client);


        eventsToPay.forEach(payment::addEvent);


        payment.setPaymentMethod(method);


        if (voucher != null) {
            payment.applyVoucher(voucher);
        }


        Payment saved = repositoryPayment.save(payment);
        return new ResponsePayment(saved);
    }

    @Transactional
    public ResponsePayment editPayment(Long id, EditPayment body) {

        Payment payment = repositoryPayment.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado."));

        if (PaymentStatus.PAID.equals(payment.getStatus())) {
            throw new IllegalStateException("Não é possível editar um pagamento já pago.");
        }


        if (body.status() != null) {
            payment.setStatus(body.status());
        }

        if (body.paymentMethod() != null) {
            if(body.paymentMethod() != payment.getPaymentMethod()) {
                payment.setPaymentMethod(body.paymentMethod());
            }
        }


        Payment updated = repositoryPayment.save(payment);
        return new ResponsePayment(updated);
    }


    @Transactional
    public List<ResponsePayment> findAll() {

        return repositoryPayment.findAll().stream().map(ResponsePayment::new).toList();
    }


    @Transactional
    public List<ResponsePayment> findByClientId(Long idClient) {

        return repositoryPayment.findClientId(idClient).stream().map(ResponsePayment::new).toList();
    }
}

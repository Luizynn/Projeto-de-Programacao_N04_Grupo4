package com.pagamentos.projeto_programacao.payment.DTO;

import com.pagamentos.projeto_programacao.payment.Payment;
import com.pagamentos.projeto_programacao.payment.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;

public record ResponsePayment(
        Long id,
        PaymentStatus status,
        List<BigDecimal> priceAmount,
        BigDecimal totalAmount,
        String paymentMethod,
        Long idClient,
        String nameClient

) {

    public ResponsePayment (Payment payment) {
        this (
                payment.getId(),
                payment.getStatus(),
                payment.getPriceAmount(),
                payment.getTotalAmount(),
                payment.getPaymentMethod(),
                payment.getClient().getId(),
                payment.getClient().getName()
        );
    }
}

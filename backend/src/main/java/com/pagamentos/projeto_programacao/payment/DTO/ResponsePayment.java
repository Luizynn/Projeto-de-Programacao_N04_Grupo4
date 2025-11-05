package com.pagamentos.projeto_programacao.payment.DTO;

import com.pagamentos.projeto_programacao.payment.Payment;
import com.pagamentos.projeto_programacao.payment.PaymentMethod;
import com.pagamentos.projeto_programacao.payment.PaymentStatus;

import java.math.BigDecimal;



public record ResponsePayment(
        Long id,
        PaymentStatus status,
        PaymentMethod paymentMethod,
        Long idClient,
        String nameClient,


        BigDecimal subtotal,
        BigDecimal paymentFee,
        BigDecimal discountAmount,
        BigDecimal totalAmount
) {


    public ResponsePayment (Payment payment) {
        this (
                payment.getId(),
                payment.getStatus(),
                payment.getPaymentMethod(),
                payment.getClient().getId(),
                payment.getClient().getName(),
                payment.getSubtotal(),
                payment.getPaymentFee(),
                payment.getDiscountAmount(),
                payment.getTotalAmount()
        );
    }
}
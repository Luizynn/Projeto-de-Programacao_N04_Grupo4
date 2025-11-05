package com.pagamentos.projeto_programacao.payment.DTO;

import com.pagamentos.projeto_programacao.payment.PaymentMethod;
import com.pagamentos.projeto_programacao.payment.PaymentStatus;

import java.math.BigDecimal;

public record EditPayment(
        PaymentStatus status,
        PaymentMethod paymentMethod,
        BigDecimal totalAmount
) { }

package com.pagamentos.projeto_programacao.payment.DTO;

import java.math.BigDecimal;

public record EditPayment(
        String status,
        String paymentMethod,
        BigDecimal totalAmount
) { }

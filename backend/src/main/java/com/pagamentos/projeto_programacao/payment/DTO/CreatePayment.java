package com.pagamentos.projeto_programacao.payment.DTO;


import com.pagamentos.projeto_programacao.payment.PaymentMethod;

import java.util.List;

public record CreatePayment(
        Long id_client,

        PaymentMethod paymentMethod,

        List<Long> eventId,

        String voucherCode,

        String cardNumber,
        String expirationMonth,
        String expirationYear,
        String cvv
) {
}
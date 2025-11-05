package com.pagamentos.projeto_programacao.payment;

import java.math.BigDecimal;

public enum PaymentMethod {
    CREDIT_CARD(new BigDecimal("0.05")),
    PIX(new BigDecimal("-0.10")),
    BOLETO(BigDecimal.ZERO);

    private final BigDecimal adjustmentFactor;

    PaymentMethod(BigDecimal adjustmentFactor){
        this.adjustmentFactor = adjustmentFactor;
    }

    public BigDecimal getAdjustmentFactor(){
        return adjustmentFactor;
    }

    public BigDecimal calculateAdjustedTotal(BigDecimal subtotal) {
        if (subtotal == null || subtotal.compareTo(BigDecimal.ZERO) == 0) {
            return subtotal;
        }

        BigDecimal adjustmentAmount = subtotal.multiply(this.adjustmentFactor);

        return subtotal.add(adjustmentAmount);
    }


}

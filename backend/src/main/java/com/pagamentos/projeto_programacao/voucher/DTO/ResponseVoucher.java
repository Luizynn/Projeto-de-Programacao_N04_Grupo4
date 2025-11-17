package com.pagamentos.projeto_programacao.voucher.DTO;

import com.pagamentos.projeto_programacao.voucher.Voucher;

import java.math.BigDecimal;

public record ResponseVoucher(
        Long id,
        String cupom,
        Integer qtUsed,
        BigDecimal discount
) {

    public ResponseVoucher(Voucher voucher) {
        this(
                voucher.getId(),
                voucher.getCupom(),
                voucher.getQtUsed(),
                voucher.getDiscount()
        );
    }
}

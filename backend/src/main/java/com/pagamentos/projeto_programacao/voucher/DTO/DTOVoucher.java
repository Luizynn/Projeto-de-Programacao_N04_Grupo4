package com.pagamentos.projeto_programacao.voucher.DTO;

import com.pagamentos.projeto_programacao.voucher.Voucher;

import java.math.BigDecimal;

public record DTOVoucher(Long id, String cupom, int qt_used, BigDecimal discount) {
    public DTOVoucher(Voucher voucher) {
        this (
                voucher.getId(),
                voucher.getCupom(),
                voucher.getQtUsed(),
                voucher.getDiscount()
        );
    }
}

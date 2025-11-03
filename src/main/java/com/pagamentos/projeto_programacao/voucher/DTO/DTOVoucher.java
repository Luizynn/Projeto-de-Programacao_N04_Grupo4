package com.pagamentos.projeto_programacao.voucher.DTO;

import com.pagamentos.projeto_programacao.voucher.Voucher;

public record DTOVoucher(Long id, String cupom, int qt_used, float discount) {
    public DTOVoucher(Voucher voucher) {
        this (
                voucher.getId(),
                voucher.getCupom(),
                voucher.getQtUsed(),
                voucher.getDiscount()
        );
    }
}

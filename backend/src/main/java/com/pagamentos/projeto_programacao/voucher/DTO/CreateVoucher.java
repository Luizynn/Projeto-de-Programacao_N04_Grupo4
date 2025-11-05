package com.pagamentos.projeto_programacao.voucher.DTO;

import java.math.BigDecimal;

public record CreateVoucher(String cupom, int qt_used, BigDecimal discount) {
}

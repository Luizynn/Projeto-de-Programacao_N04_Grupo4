package com.pagamentos.projeto_programacao.voucher;

import com.pagamentos.projeto_programacao.voucher.DTO.CreateVoucher;
import com.pagamentos.projeto_programacao.voucher.DTO.DTOVoucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceVoucher {
    @Autowired
    private RepositoryVoucher repositoryVoucher;

    @Transactional
    public DTOVoucher create (CreateVoucher body) {
        Voucher voucher = new Voucher();

        voucher.setDiscount(body.discount());
        voucher.setCupom(body.cupom());
        voucher.setQtUsed(body.qt_used());

        return new DTOVoucher(repositoryVoucher.save(voucher));
    }

}

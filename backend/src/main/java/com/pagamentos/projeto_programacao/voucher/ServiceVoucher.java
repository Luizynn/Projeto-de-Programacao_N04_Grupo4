package com.pagamentos.projeto_programacao.voucher;

import com.pagamentos.projeto_programacao.payment.DTO.ResponsePayment;
import com.pagamentos.projeto_programacao.voucher.DTO.CreateVoucher;
import com.pagamentos.projeto_programacao.voucher.DTO.DTOVoucher;
import com.pagamentos.projeto_programacao.voucher.DTO.ResponseVoucher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public List<ResponseVoucher> listar () {
        Voucher voucher = new Voucher();

        return repositoryVoucher.findAll().stream().map(ResponseVoucher::new).toList();

    }

    @Transactional
    public ResponseVoucher listByCupom (String cupom) {
        System.out.println(cupom);
        Voucher voucher = repositoryVoucher.findByCupom(cupom).orElseThrow(()->new EntityNotFoundException("Cupom não existe"));

        return new ResponseVoucher(voucher);

    }

}

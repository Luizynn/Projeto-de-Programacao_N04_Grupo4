package com.pagamentos.projeto_programacao.voucher;

import com.pagamentos.projeto_programacao.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepositoryVoucher extends JpaRepository<Voucher,Long> {
    Optional<Voucher> findByCupom(String cupom);
}

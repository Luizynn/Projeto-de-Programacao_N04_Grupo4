package com.pagamentos.projeto_programacao.voucher;

import com.pagamentos.projeto_programacao.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryVoucher extends JpaRepository<Voucher,Long> {
    List<Event> findByCupom(String cupom);
}

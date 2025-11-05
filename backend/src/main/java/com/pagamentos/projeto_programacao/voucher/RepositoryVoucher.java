package com.pagamentos.projeto_programacao.voucher;


import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface RepositoryVoucher extends JpaRepository<Voucher,Long> {
    Optional<Voucher> findByCupom(String cupom);
}

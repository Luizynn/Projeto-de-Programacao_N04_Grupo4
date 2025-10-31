package com.pagamentos.projeto_programacao.payment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryPayment extends JpaRepository<Payment,Long> {
}

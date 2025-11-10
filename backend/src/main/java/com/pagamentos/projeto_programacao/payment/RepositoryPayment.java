package com.pagamentos.projeto_programacao.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryPayment extends JpaRepository<Payment,Long> {
    List<Payment> findByClientId(Long paymentId);
    List<Payment> findByStatus(PaymentStatus status);
}

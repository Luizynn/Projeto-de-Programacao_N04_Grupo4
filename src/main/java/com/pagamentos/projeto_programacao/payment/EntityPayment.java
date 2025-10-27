package com.pagamentos.projeto_programacao.payment;
import jakarta.persistence.*;

@Entity(name = "Payment")
@Table(name = "tb_payment")
public class EntityPayment {

    @Id
    private String id;

    @Column(name = "status_payment", nullable = false, length = 40)
    private String status;

    @Column(name = "ticket_value", nullable = false, scale = 2)
    private double ticketValue;

    @Column()
    private String paymentMethod;



}

package com.pagamentos.projeto_programacao.payment;

import com.pagamentos.projeto_programacao.users.ModelUser;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;



@Entity(name = "Payment")
@Table(name = "tb_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status_payment", nullable = false, length = 40)
    private String status;

    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount;

    @Column(name = "ticket_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal ticketValue;

    @Column(name = "payment_method", nullable = false, length = 255)
    private String paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client",nullable = false)
    private ModelUser user;

    private static final List<String> VALID_PAYMENT_METHODS = Arrays.asList("cartao de credito", "pix", "cartao de debito");
    private static final String STATUS_IN_PROGRESS = "Em progresso";
    private static final String STATUS_PAID = "Pago";

    public ModelUser getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getTicketValue() {
        return ticketValue;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setClient(ModelUser user) {
        this.user = user;
    }

    public void setTicketValue(BigDecimal ticketValue) {
        if(ticketValue.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("O valor do ticket deve ser positivo.");
        }
        this.ticketValue = ticketValue;
    }

    public void setPaymentMethod(String paymentMethod) {
        if(paymentMethod == null || !VALID_PAYMENT_METHODS.contains(paymentMethod.toLowerCase())){
            throw new IllegalArgumentException("Método de pagamento inválido");
        }
        this.paymentMethod = paymentMethod;
    }

    public void generatePayment(BigDecimal ticketValue, String paymentMethod){
        if(ticketValue.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("O valor do ticket deve ser positivo");
        }
        if(paymentMethod == null || !VALID_PAYMENT_METHODS.contains(paymentMethod.toLowerCase())){
            throw new IllegalArgumentException("Método de pagamento inválido");
        }

        this.ticketValue = ticketValue;
        this.paymentMethod = paymentMethod;
        this.status = STATUS_IN_PROGRESS;



    }


}

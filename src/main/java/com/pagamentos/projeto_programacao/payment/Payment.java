package com.pagamentos.projeto_programacao.payment;

import com.pagamentos.projeto_programacao.users.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@Entity(name = "Payment")
@Table(name = "tb_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount", precision = 10, scale = 2)
    private BigDecimal discount;

    @Transient
    private List<BigDecimal> priceAmount = new ArrayList<>();

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_method", nullable = false, length = 255)
    private String paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client",nullable = false)
    private User client;

    private static final List<String> VALID_PAYMENT_METHODS = Arrays.asList("cartao de credito", "pix", "cartao de debito");


    @Enumerated(EnumType.STRING)
    @Column(name = "status_payment", nullable = false, length = 40)
    private PaymentStatus status;

    public User getClient() {
        return client;
    }

    public void setUser(User client) {
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public List<BigDecimal> getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(List<BigDecimal> priceAmount) {
        this.priceAmount = priceAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }


    public void setClient(User client) {
        this.client = client;
    }


    public void setPaymentMethod(String paymentMethod) {
        if(paymentMethod == null || !VALID_PAYMENT_METHODS.contains(paymentMethod.toLowerCase())){
            throw new IllegalArgumentException("Método de pagamento inválido");
        }
        this.paymentMethod = paymentMethod;
    }

    public void calculateTotal() {
        if (priceAmount != null && !priceAmount.isEmpty()) {
            this.totalAmount = priceAmount.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.totalAmount = BigDecimal.ZERO;
        }
    }

    public void generatePayment(String paymentMethod){
        if(totalAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("O valor do ticket deve ser positivo");
        }
        if(paymentMethod == null || !VALID_PAYMENT_METHODS.contains(paymentMethod.toLowerCase())){
            throw new IllegalArgumentException("Método de pagamento inválido");
        }

        this.totalAmount = BigDecimal.ZERO;
        this.paymentMethod = paymentMethod;
        this.status = PaymentStatus.IN_PROGRESS;



    }

    public void addPrice(BigDecimal price) {
        if (price.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("O valor deve ser positivo");
        }
        this.priceAmount.add(price);
        calculateTotal();
    }

    public void addDiscount(BigDecimal discount, BigDecimal totalAmount){
        if(discount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("O desconto não existe");
        }
        if(discount.compareTo(BigDecimal.ZERO) > 0){
            throw new IllegalArgumentException("O desconto não pode ser maior que 1 (100%)");
        }

        BigDecimal discountedValue = totalAmount.multiply(discount);
        BigDecimal total = totalAmount.subtract(discountedValue);

        setTotalAmount(total.setScale(2, RoundingMode.HALF_UP));
    }

    public void pay(){
        if(this.totalAmount == null || this.totalAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalStateException("Não há eventos a serem pagos");
        }

        if (PaymentStatus.PAID.equals(this.status)){
            throw new IllegalStateException("O pagamento já foi realizado");
        }
        setStatus(PaymentStatus.PAID);
    }

    public void cancel() {
        if (this.status == PaymentStatus.PAID){
            throw new IllegalStateException("Pagamento já concluído");
        }
        this.status = PaymentStatus.CANCELLED;
    }


}

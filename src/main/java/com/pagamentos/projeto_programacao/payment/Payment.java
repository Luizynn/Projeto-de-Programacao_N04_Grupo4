package com.pagamentos.projeto_programacao.payment;

import com.pagamentos.projeto_programacao.users.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @ElementCollection
    @CollectionTable(
            name = "payment_price_amount",
            joinColumns = @JoinColumn(name = "payment_id")
    )
    @Column(name = "price_amount")
    private List<BigDecimal> priceAmount;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_method", nullable = false, length = 255)
    private String paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client",nullable = false)
    private User client;

    private static final List<String> VALID_PAYMENT_METHODS = Arrays.asList("cartao de credito", "pix", "cartao de debito");
    private static final String STATUS_IN_PROGRESS = "Em progresso";
    private static final String STATUS_PAID = "Pago";

    public User getClient() {
        return client;
    }

    public void setUser(User client) {
        this.client = client;
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

    public void setStatus(String status) {
        this.status = status;
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

    public void generatePayment(BigDecimal totalAmount, String paymentMethod){
        if(totalAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("O valor do ticket deve ser positivo");
        }
        if(paymentMethod == null || !VALID_PAYMENT_METHODS.contains(paymentMethod.toLowerCase())){
            throw new IllegalArgumentException("Método de pagamento inválido");
        }

        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = STATUS_IN_PROGRESS;



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

        if (STATUS_PAID.equals(this.status)){
            throw new IllegalStateException("O pagamento já foi realizado");
        }
        setStatus(STATUS_PAID);
    }


}

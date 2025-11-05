package com.pagamentos.projeto_programacao.payment;

import com.pagamentos.projeto_programacao.event.Event;
import com.pagamentos.projeto_programacao.users.User;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.pagamentos.projeto_programacao.voucher.Voucher;

@Entity(name = "Payment")
@Table(name = "tb_payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subtotal", precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "payment_fee", precision = 10, scale = 2)
    private BigDecimal paymentFee;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_payment_events",
            joinColumns = @JoinColumn(name = "payment_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<Event> events = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 50)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false)
    private User client;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_voucher")
    private Voucher voucher;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_payment", nullable = false, length = 40)
    private PaymentStatus status;

    public void addEvent(Event event) {
        if (event == null || event.getPrice() == null || event.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O evento ou seu preço é inválido");
        }
        this.events.add(event);
        recalculateTotals();
    }


    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new IllegalArgumentException("Método de pagamento não pode ser nulo");
        }
        this.paymentMethod = paymentMethod;
        recalculateTotals();
    }


    public void applyVoucher(Voucher voucher) {
        if (voucher == null) {
            this.voucher = null;
        } else {

            if (voucher.getDiscount().compareTo(BigDecimal.ZERO) < 0 ||
                    voucher.getDiscount().compareTo(BigDecimal.ONE) > 0) {
                throw new IllegalArgumentException("Percentual de desconto do voucher é inválido.");
            }
            this.voucher = voucher;
        }
        recalculateTotals();
    }


    private void recalculateTotals() {

        this.subtotal = this.events.stream()
                .map(Event::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        ;

        BigDecimal currentTotal = this.subtotal;


        if (this.paymentMethod != null) {
            BigDecimal factor = this.paymentMethod.getAdjustmentFactor();
            this.paymentFee = this.subtotal.multiply(factor);
            currentTotal = currentTotal.add(this.paymentFee);
        } else {
            this.paymentFee = BigDecimal.ZERO;
        }


        if (this.voucher != null) {

            this.discountAmount = currentTotal.multiply(this.voucher.getDiscount());
            currentTotal = currentTotal.subtract(this.discountAmount);
        } else {
            this.discountAmount = BigDecimal.ZERO;
        }


        this.totalAmount = currentTotal.setScale(2, RoundingMode.HALF_UP);


        if (this.status == null) {
            this.status = PaymentStatus.IN_PROGRESS;
        }
    }

    public void pay() {

        if (this.status == PaymentStatus.PAID) {
            throw new IllegalStateException("O pagamento já foi realizado.");
        }

        if (this.status == PaymentStatus.CANCELLED) {
            throw new IllegalStateException("Não é possível pagar um pedido cancelado.");
        }

        if (this.totalAmount == null || this.totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Não há valor a ser pago.");
        }

        this.status = PaymentStatus.PAID;
    }


    public void cancel() {

        if (this.status == PaymentStatus.PAID) {
            throw new IllegalStateException("Não é possível cancelar um pagamento já concluído.");
        }

        if (this.status == PaymentStatus.CANCELLED) {
            throw new IllegalStateException("O pagamento já está cancelado.");
        }
        this.status = PaymentStatus.CANCELLED;
    }


    public Long getId() {
        return id;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public User getClient() {
        return client;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getPaymentFee() {
        return paymentFee;
    }

    public List<Event> getEvents() {
        return events;
    }

}
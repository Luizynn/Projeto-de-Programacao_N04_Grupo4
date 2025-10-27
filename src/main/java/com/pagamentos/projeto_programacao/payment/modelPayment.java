package com.pagamentos.projeto_programacao.payment;

import com.pagamentos.projeto_programacao.client.ModelClient;
import com.pagamentos.projeto_programacao.receipt.ModelReceipt;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class modelPayment {

    private String id;
    private String status;
    private double ticketValue;
    private String paymentMethod;

    private ModelClient client;

    private static final List<String> VALID_PAYMENT_METHODS = Arrays.asList("cartao de credito", "pix", "cartao de debito");
    private static final String STATUS_IN_PROGRESS = "Em progresso";
    private static final String STATUS_PAID = "Pago";

    public ModelClient getClient() {
        return client;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public double getTicketValue() {
        return ticketValue;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTicketValue(double ticketValue) {
        if(ticketValue <= 0){
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

    public void generatePayment(double ticketValue, String paymentMethod){
        if(ticketValue <= 0){
            throw new IllegalArgumentException("O valor do ticket deve ser positivo");
        }
        if(paymentMethod == null || !VALID_PAYMENT_METHODS.contains(paymentMethod.toLowerCase())){
            throw new IllegalArgumentException("Método de pagamento inválido");
        }

        this.ticketValue = ticketValue;
        this.paymentMethod = paymentMethod;
        this.status = STATUS_IN_PROGRESS;


        this.id = UUID.randomUUID().toString().substring(0, 8);
    }

    public void computeDiscount(double discount){
        if(discount > 1){
            discount = discount / 100;
        }
        double valueAfterDiscount = this.ticketValue * discount;
        setTicketValue(this.ticketValue - valueAfterDiscount);
    }

    public ModelReceipt computePayment(){
        setStatus(STATUS_PAID);
        ModelReceipt newReceipt = new ModelReceipt(this, null);
        return newReceipt;
    }
}

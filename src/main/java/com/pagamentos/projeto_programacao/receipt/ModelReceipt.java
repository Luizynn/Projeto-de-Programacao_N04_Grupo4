package com.pagamentos.projeto_programacao.receipt;

import com.pagamentos.projeto_programacao.Subscription.ModelSubscription;
import com.pagamentos.projeto_programacao.client.ModelClient;
import com.pagamentos.projeto_programacao.helpers.Helpers;
import com.pagamentos.projeto_programacao.payment.modelPayment;

import java.time.LocalDateTime;

public class ModelReceipt {
    private Long id;
    private String data;
    private String hour;


    private modelPayment payment;
    private ModelClient client;
    private ModelSubscription subscription;


    public ModelReceipt(modelPayment payment, ModelSubscription subscription) {
        this.data = Helpers.getTodayDate();
        this.hour = Helpers.getCurrentHour();

        this.payment = payment;
        this.subscription = subscription;

        this.client = payment.getClient();

    }

    public Long getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public modelPayment getPayment() {
        return payment;
    }

    public void setPayment(modelPayment payment) {
        this.payment = payment;
    }

    public ModelClient getClient() {
        return client;
    }

    public void setClient(ModelClient client) {
        this.client = client;
    }

    public ModelSubscription getSubscription() {
        return subscription;
    }

    public void setSubscription(ModelSubscription subscription) {
        this.subscription = subscription;
    }

    public String emitir(){
        if(payment == null || payment.getStatus() == null){
            return "Pagamento inválido. Nenhuma informação encontrada.";
        }
        if(payment.getStatus().equals("Em progresso")){
           return "O pagamento ainda não foi realizado, tente novamente";
        }
        return "Pagamento " + this.payment.getId() + " do cliente" + this.getClient() + " no valor de: " + this.payment.getTicketValue() + " realizado com sucesso" + "\n Hora: " + Helpers.getCurrentHour() + "\n Dia: " + Helpers.getTodayDate();
    }

    public void cancelarNotaFiscal() {
        if (payment == null || payment.getStatus() == null) {
            throw new IllegalStateException("Recibo inválido. Não há pagamento associado.");
        }

        if (payment.getStatus().equalsIgnoreCase("Em progresso")) {
            throw new IllegalStateException("O pagamento ainda não foi concluído. Não é possível cancelar.");
        }

        if (payment.getStatus().equalsIgnoreCase("Cancelado")) {
            throw new IllegalStateException("A nota fiscal já foi cancelada anteriormente.");
        }

        // Atualiza status do pagamento
        payment.setStatus("Cancelado");

        // Atualiza informações do recibo
        this.data = Helpers.getTodayDate();
        this.hour = Helpers.getCurrentHour();

        System.out.println("Nota fiscal do pagamento " + payment.getId() + " foi cancelada com sucesso.");
    }
}
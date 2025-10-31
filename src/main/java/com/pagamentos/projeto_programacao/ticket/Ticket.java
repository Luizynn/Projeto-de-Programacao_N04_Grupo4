package com.pagamentos.projeto_programacao.ticket;

import java.util.UUID;

public class Ticket {

    private String id;
    private String eventName;
    private String clientName;
    private double price;
    private String status;

    private static final String STATUS_CREATED = "Criado";
    private static final String STATUS_RESERVED = "Reservado";
    private static final String STATUS_USED = "Utilizado";
    private static final String STATUS_CANCELED = "Cancelado";

    // GETTERS
    public String getId() {
        return id;
    }

    public String getEventName() {
        return eventName;
    }

    public String getClientName() {
        return clientName;
    }

    public double getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    // SETTERS COM VALIDAÇÃO
    public void setEventName(String eventName) {
        if(eventName == null || eventName.isEmpty()){
            throw new IllegalArgumentException("O nome do evento não pode estar vazio.");
        }
        this.eventName = eventName;
    }

    public void setClientName(String clientName) {
        if(clientName == null || clientName.isEmpty()){
            throw new IllegalArgumentException("O nome do cliente não pode estar vazio.");
        }
        this.clientName = clientName;
    }

    public void setPrice(double price) {
        if(price <= 0){
            throw new IllegalArgumentException("O valor do ticket deve ser positivo.");
        }
        this.price = price;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    // MÉTODO PARA GERAR O TICKET
    public void generateTicket(String eventName, String clientName, double price){
        setEventName(eventName);
        setClientName(clientName);
        setPrice(price);

        this.id = UUID.randomUUID().toString().substring(0, 8);
        setStatus(STATUS_CREATED);
    }

    // RESERVAR TICKET
    public void reserveTicket(){
        if(!status.equals(STATUS_CREATED)){
            throw new IllegalStateException("O ticket só pode ser reservado se estiver 'Criado'.");
        }
        setStatus(STATUS_RESERVED);
    }

    // UTILIZAR O TICKET
    public void useTicket(){
        if(!status.equals(STATUS_RESERVED)){
            throw new IllegalStateException("O ticket só pode ser utilizado se estiver 'Reservado'.");
        }
        setStatus(STATUS_USED);
    }

    // CANCELAR TICKET
    public void cancelTicket(){
        if(status.equals(STATUS_USED)){
            throw new IllegalStateException("O ticket não pode ser cancelado após ser utilizado.");
        }
        setStatus(STATUS_CANCELED);
    }
}

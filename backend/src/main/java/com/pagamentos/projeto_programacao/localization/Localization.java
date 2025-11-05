package com.pagamentos.projeto_programacao.localization;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_localization")
public class Localization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "cep",length = 8,nullable = false)
    private String cep;

    @Column(name = "address",length = 255,nullable = false)
    private String address;

    @Column(name = "number",length = 10)
    private String number;

    @Column(name = "reference",length = 80)
    private String reference;

    @Column(name = "neighborhood",length = 80, nullable = false)
    private String neighborhood;

    public Localization() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}

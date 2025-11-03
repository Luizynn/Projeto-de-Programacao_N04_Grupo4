package com.pagamentos.projeto_programacao.voucher;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_voucher")

public class Voucher {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "cupom",nullable = false,length = 8)
    private String cupom;

    @Column(name = "qt_used",nullable = false)
    private int qtUsed;

    @Column(name = "discount",nullable = false)
    private float discount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCupom() {
        return cupom;
    }

    public void setCupom(String cupom) {
        this.cupom = cupom;
    }

    public int getQtUsed() {
        return qtUsed;
    }

    public void setQtUsed(int qtUsed) {
        this.qtUsed = qtUsed;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}

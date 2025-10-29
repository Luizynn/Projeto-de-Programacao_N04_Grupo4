package com.pagamentos.projeto_programacao.event;

import com.pagamentos.projeto_programacao.localization.Localization;
import com.pagamentos.projeto_programacao.users.User;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_event")
public class Event implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_organizer",nullable = false)
    private User organizer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_localization",nullable = false)
    private Localization localization;

    @Column(name = "name",nullable = false,length = 80)
    private String name;

    @Column(name = "dt_start",nullable = false)
    private LocalDateTime dtStart;

    @Column(name = "dt_end",nullable = false)
    private LocalDateTime dtEnd;

    @Column(name = "price",nullable = false)
    private BigDecimal price;

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public Localization getLocalization() {
        return localization;
    }

    public void setLocalization(Localization localization) {
        this.localization = localization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDtStart() {
        return dtStart;
    }

    public void setDtStart(LocalDateTime dtStart) {
        this.dtStart = dtStart;
    }

    public LocalDateTime getDtEnd() {
        return dtEnd;
    }

    public void setDtEnd(LocalDateTime dtEnd) {
        this.dtEnd = dtEnd;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}

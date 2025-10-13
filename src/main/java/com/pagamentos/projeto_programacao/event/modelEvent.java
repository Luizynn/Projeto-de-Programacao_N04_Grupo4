package com.pagamentos.projeto_programacao.event;

import java.util.Date;

public class modelEvent {
    private String id;
    private String name;
    private Date date;
    private float value;

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}


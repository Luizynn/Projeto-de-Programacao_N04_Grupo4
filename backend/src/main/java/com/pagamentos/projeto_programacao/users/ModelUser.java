package com.pagamentos.projeto_programacao.users;

import com.pagamentos.projeto_programacao.helpers.Helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModelUser {
    private String id;
    private String name;
    private String cpf;
    private String email;
    private String password;
    private String dtBirth;

    public ModelUser() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws Exception {
        if(name.length() < 4)
            throw new Exception("Nome precisa ter no minimo 4 cacteres");

        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) throws Exception {
        if(!Helpers.checkCpf(cpf))
            throw new Exception("Cpf invalido");

        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception {
        Pattern pattern = Pattern.compile("\\S+@\\S+\\.\\S+",Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(email);

        if(!matcher.find())
            throw new Exception("Email invalido");

        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDtBirth() {
        return dtBirth;
    }

    public void setDtBirth(String dtBirth) {
        this.dtBirth = dtBirth;
    }
}

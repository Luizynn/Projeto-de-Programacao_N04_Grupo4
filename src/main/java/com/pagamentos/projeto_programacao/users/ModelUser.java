package com.pagamentos.projeto_programacao.users;

public class ModelUser {
    private String id;
    private String name;
    private String cpf;
    private String email;
    private String senha;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getDtBirth() {
        return dtBirth;
    }

    public void setDtBirth(String dtBirth) {
        this.dtBirth = dtBirth;
    }
}

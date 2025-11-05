package com.pagamentos.projeto_programacao.users.DTO;

import java.util.Date;

public record CreateUser(String name, String cpf, String email, String password, Date dtBirth) {
}

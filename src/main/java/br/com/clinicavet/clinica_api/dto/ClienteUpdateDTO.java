package br.com.clinicavet.clinica_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ClienteUpdateDTO {




    private String nome;


    private String cpf;

    private LocalDate dataNascimento;

    private String telefone;

    private String email;
}

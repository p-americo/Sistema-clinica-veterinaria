package br.com.clinicavet.clinica_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ClienteUpdateDTO {

    @NotBlank
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotBlank
    private LocalDate dataNascimento;

    @NotBlank
    private String telefone;

    @NotBlank
    private String email;
}

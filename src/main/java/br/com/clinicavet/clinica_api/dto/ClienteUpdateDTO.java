package br.com.clinicavet.clinica_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClienteUpdateDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cpf;

    @NotNull
    private LocalDate dataNascimento;

    @NotBlank
    private String telefone;

    @NotBlank
    private String email;
}

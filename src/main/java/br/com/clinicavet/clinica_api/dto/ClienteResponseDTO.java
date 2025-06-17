package br.com.clinicavet.clinica_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClienteResponseDTO {


    private Long id;

    private String nome;

    private String cpf;

    private LocalDate dataNascimento;

    private String telefone;

    private String email;

    private LocalDate dataCadastro;
}

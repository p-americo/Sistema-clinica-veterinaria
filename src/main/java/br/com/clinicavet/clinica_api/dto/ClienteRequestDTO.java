package br.com.clinicavet.clinica_api.dto;

import br.com.clinicavet.clinica_api.model.EAnimal;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.List;

public class ClienteRequestDTO {


    @NotBlank(message = "O nome não pode ser nulo")
    private String nome;

    @NotBlank(message = "O cpf não pode ser nulo")
    private String cpf;

    @NotBlank(message = "A Data de Nascimento não pode ser nula")
    private LocalDate dataNascimento;

    @NotBlank(message = "O telefone não pode ser nulo")
    private String telefone;

    @NotBlank(message = "O email não pode ser nulo")
    private String email;


}

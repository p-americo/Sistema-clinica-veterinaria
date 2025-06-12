package br.com.clinicavet.clinica_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AgendamentoResponseDTO {

    private Long id;
    private LocalDateTime dataAgendamento;
    private LocalDateTime dataCriacao;
    private String status;

    private String nomeAnimal;
    private String nomePessoa;
}

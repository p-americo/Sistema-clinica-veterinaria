package br.com.clinicavet.clinica_api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InternacaoResponseDTO {

    private Long id;

    private Long animalId;

    private LocalDateTime dataEntrada;

    private LocalDateTime dataSaida;

    private String status;
}

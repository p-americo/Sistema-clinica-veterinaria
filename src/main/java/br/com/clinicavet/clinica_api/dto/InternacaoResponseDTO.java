package br.com.clinicavet.clinica_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InternacaoResponseDTO {
    
    private Long id;
    private Long animalId;
    private String nomeAnimal;
    private Long veterinarioResponsavelId;
    private String nomeVeterinario;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String motivoInternacao;
    private String observacoes;
    private Boolean ativa;
}
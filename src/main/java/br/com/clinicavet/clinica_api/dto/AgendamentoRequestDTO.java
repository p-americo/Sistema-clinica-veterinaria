package br.com.clinicavet.clinica_api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AgendamentoRequestDTO {

    @NotNull(message = "A data do agendamento é obrigatória")
    @Future(message = "A data do agendamento deve estar no futuro")
    private LocalDateTime dataAgendamento;

    @NotNull(message = "O ID do animal é obrigatório")
    private Long animalId;

    @NotNull(message = "O ID da pessoa é obrigatório")
    private Long pessoaId;

    private String status = "AGENDADO";
}

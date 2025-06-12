package br.com.clinicavet.clinica_api.dto;

import br.com.clinicavet.clinica_api.model.enums.StatusAgendamento;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AgendamentoResponseDTO {

    private Long id;
    private AnimalResponseDTO animal; // DTO aninhado para mostrar detalhes do animal
    private ServicoResponseDTO servico; // DTO aninhado para detalhes do serviço
    private ClienteResponseDTO cliente; // DTO aninhado para detalhes do cliente
    private LocalDateTime dataHoraAgendamento;
    private StatusAgendamento status;
    private String observacoes;
}

package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.AgendamentoRequestDTO;
import br.com.clinicavet.clinica_api.dto.AgendamentoResponseDTO;
import br.com.clinicavet.clinica_api.dto.AgendamentoUpdateDTO;

import java.util.List;

public interface AgendamentoService {

    AgendamentoResponseDTO criarAgendamento(AgendamentoRequestDTO requestDTO);

    AgendamentoResponseDTO atualizarAgendamento(Long id, AgendamentoUpdateDTO updateDTO);

    AgendamentoResponseDTO deletarAgendamento(Long id);

    List<AgendamentoResponseDTO> listarTodos();

    AgendamentoResponseDTO listarPorId(Long id);

    AgendamentoResponseDTO cancelarAgendamento(Long id);

    AgendamentoResponseDTO confirmarAgendamento(Long id);


}

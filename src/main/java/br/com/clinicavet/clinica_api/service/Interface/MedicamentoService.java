package br.com.clinicavet.clinica_api.service.Interface;

import br.com.clinicavet.clinica_api.dto.MedicamentoResponseDTO;

public interface MedicamentoService {

    MedicamentoResponseDTO buscarTodos(Long id);

    MedicamentoResponseDTO buscarPorId(Long id);
}

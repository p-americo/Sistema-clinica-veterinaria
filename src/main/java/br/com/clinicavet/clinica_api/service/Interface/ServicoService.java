package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.ServicoRequestDTO;
import br.com.clinicavet.clinica_api.dto.ServicoResponseDTO;

import java.util.List;

public interface ServicoService {

    ServicoResponseDTO buscarPorId(Long id);

    List<ServicoResponseDTO> buscarTodos(String nome);

    ServicoResponseDTO cadastrarServico(String nome);

    ServicoResponseDTO atualizarServico(Long id, ServicoRequestDTO update);

    ServicoResponseDTO deletarServico(Long id);




}

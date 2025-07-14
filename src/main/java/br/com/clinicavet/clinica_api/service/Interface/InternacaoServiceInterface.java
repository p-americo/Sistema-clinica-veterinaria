package br.com.clinicavet.clinica_api.service.Interface;

import br.com.clinicavet.clinica_api.dto.InternacaoRequestDTO;
import br.com.clinicavet.clinica_api.dto.InternacaoResponseDTO;
import br.com.clinicavet.clinica_api.dto.InternacaoUpdateDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface InternacaoServiceInterface {
    
    InternacaoResponseDTO criarInternacao(InternacaoRequestDTO internacaoRequestDTO);
    
    InternacaoResponseDTO atualizarInternacao(Long id, InternacaoUpdateDTO internacaoUpdateDTO);
    
    void deletarInternacao(Long id);
    
    InternacaoResponseDTO buscarPorId(Long id);
    
    List<InternacaoResponseDTO> buscarInternacoesAtivas();
    
    List<InternacaoResponseDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
    
    List<InternacaoResponseDTO> listarTodos();
    
    InternacaoResponseDTO finalizarInternacao(Long id);
}